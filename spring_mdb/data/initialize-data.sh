until mongosh "mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=rs0" --eval "db.runCommand({ ping: 1 })" &> /dev/null; do
  sleep 1
done

mongoimport --uri="mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=rs0" --db=football --collection=teams --jsonArray < teams.json
mongoimport --uri="mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=rs0" --db=football --collection=players --jsonArray < players.json
mongoimport --uri="mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=rs0" --db=football --collection=matches --jsonArray < matches.json
mongoimport --uri="mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=rs0" --db=football --collection=match_events --jsonArray < events.json
