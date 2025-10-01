#!/usr/bin/env bash
set -euo pipefail

COMPOSE_FILE="docker-compose.yml"
RS_NAME="rs0"

# Internal hostnames for replica set initialization
INIT_URI="mongodb://mongo1:27017,mongo2:27017,mongo3:27017/?replicaSet=${RS_NAME}"

# Host-side URIs (only used if you run tools from host)
REPLICA_URI="mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=${RS_NAME}"
DIRECT_URI="mongodb://127.0.0.1:27017/?directConnection=true"

# Files to import (adjust paths if necessary)
TEAMS_JSON="teams.json"
PLAYERS_JSON="players.json"
MATCHES_JSON="matches.json"
EVENTS_JSON="events.json"

docker-compose -f "${COMPOSE_FILE}" up -d

echo "$(date) Waiting for any MongoDB process to accept connections..."
timeout=60
elapsed=0
until mongosh "${DIRECT_URI}" --eval "db.runCommand({ ping: 1 })" &>/dev/null; do
  if (( elapsed >= timeout )); then
    echo "$(date) Timeout waiting for mongod on 127.0.0.1:27017"
    exit 1
  fi
  sleep 1
  elapsed=$((elapsed+1))
done

function check_internal_mongo_ready() {
  local host_name="$1"
  local port="27017"
  local timeout=30
  local elapsed=0
  echo "$(date) Waiting for internal container ${host_name} to be ready..."
  until docker exec "${host_name}" mongosh "mongodb://localhost:${port}/?directConnection=true" --eval "db.runCommand({ ping: 1 })" &>/dev/null; do
    if (( elapsed >= timeout )); then
      echo "$(date) Timeout waiting for internal ping on ${host_name}"
      return 1
    fi
    sleep 1
    elapsed=$((elapsed+1))
  done
  echo "$(date) ${host_name} is internally ready."
  return 0
}

check_internal_mongo_ready mongo1
check_internal_mongo_ready mongo2
check_internal_mongo_ready mongo3

echo "$(date) All nodes are internally ready. Checking replica set initialization..."
if docker exec mongo1 mongosh --quiet --eval 'try { rs.status(); } catch(e) { quit(1); }' &>/dev/null; then
  echo "$(date) Replica set appears reachable (checked on mongo1). Skipping rs.initiate."
else
  echo "$(date) Initiating replica set on mongo1 (internal hostnames)..."
  docker exec mongo1 mongosh --quiet --eval "rs.initiate({
    _id: '${RS_NAME}',
      members: [
        { _id: 0, host: 'mongo1:27017' },
        { _id: 1, host: 'mongo2:27017' },
        { _id: 2, host: 'mongo3:27017' }
      ]
  })"
  sleep 5
fi

echo "$(date) Waiting for a PRIMARY to be elected (internal check)..."
timeout=120
elapsed=0
until docker exec mongo1 mongosh --quiet --eval 'const m = rs.status().members.find(x => x.stateStr==="PRIMARY"); if(!m) { quit(1); } else { print(m.name); }' 2>/dev/null | sed -n '1p' >/dev/null; do
  if (( elapsed >= timeout )); then
    echo "$(date) Timeout waiting for PRIMARY (internal check)"
    docker exec mongo1 mongosh --eval "printjson(rs.status())"
    exit 1
  fi
  sleep 1
  elapsed=$((elapsed+1))
done

# Get the PRIMARY member hostname (e.g. mongo3:27017)
PRIMARY_HOSTPORT=$(docker exec mongo1 mongosh --quiet --eval 'const m = rs.status().members.find(x => x.stateStr==="PRIMARY"); if(!m){ quit(1);} print(m.name);')
PRIMARY_CONTAINER="${PRIMARY_HOSTPORT%%:*}"  # extract 'mongo3' from 'mongo3:27017'
PRIMARY_PORT="${PRIMARY_HOSTPORT##*:}"       # extract '27017'

echo "$(date) PRIMARY is ${PRIMARY_CONTAINER} (${PRIMARY_HOSTPORT}). Verifying writable state..."
# quick ping and a temp write/delete to be sure writes are accepted
docker exec "${PRIMARY_CONTAINER}" mongosh --quiet --eval "db.getSiblingDB('admin').runCommand({ping:1})"
docker exec "${PRIMARY_CONTAINER}" mongosh --quiet --eval "db.getSiblingDB('football').teams.insertOne({_tmp:1}) && db.getSiblingDB('football').teams.deleteOne({_tmp:1})"

echo "$(date) PRIMARY writable. Running imports on ${PRIMARY_CONTAINER}..."
docker exec -i "${PRIMARY_CONTAINER}" mongoimport --uri="mongodb://localhost:${PRIMARY_PORT}/?directConnection=true" --db=football --collection=teams --jsonArray < "${TEAMS_JSON}"
docker exec -i "${PRIMARY_CONTAINER}" mongoimport --uri="mongodb://localhost:${PRIMARY_PORT}/?directConnection=true" --db=football --collection=players --jsonArray < "${PLAYERS_JSON}"
docker exec -i "${PRIMARY_CONTAINER}" mongoimport --uri="mongodb://localhost:${PRIMARY_PORT}/?directConnection=true" --db=football --collection=matches --jsonArray < "${MATCHES_JSON}"
docker exec -i "${PRIMARY_CONTAINER}" mongoimport --uri="mongodb://localhost:${PRIMARY_PORT}/?directConnection=true" --db=football --collection=match_events --jsonArray < "${EVENTS_JSON}"

echo "$(date) Imports complete."
