package com.packt.spring_mdb.service;

import com.packt.spring_mdb.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FootballService {
  private final TeamRepository teamRepository;
  private final MongoTemplate mongoTemplate;
  private PlayerRepository playerRepository;
  private MatchEventRepository matchEventRepository;
  private MatchRepository matchRepository;

  public FootballService(TeamRepository teamRepository, MongoTemplate mongoTemplate, PlayerRepository playerRepository, MatchEventRepository matchEventRepository, MatchRepository matchRepository) {
    this.teamRepository = teamRepository;
    this.mongoTemplate = mongoTemplate;
    this.playerRepository = playerRepository;
    this.matchEventRepository = matchEventRepository;
    this.matchRepository = matchRepository;
  }

  private static Logger log = LoggerFactory.getLogger(FootballService.class);

  // Teams

  public Team getTeam(String id) {
    return teamRepository.findById(id)
        .orElse(null);
//        .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + id));
  }

  public Team getTeamByName(String name) {
    return teamRepository.findByName(name)
        .orElse(null);
//        .orElseThrow(() -> new ResourceNotFoundException("Team not found with name: " + name));
  }

  public List<Team> getTeamsContainingName(String name) {
    return teamRepository.findByNameContaining(name);
  }

  public Team saveTeam(Team team) {
    return teamRepository.save(team);
  }

  public void deleteTeam(String id) {
    teamRepository.deleteById(id);
  }

  public List<Team> getTeamByNameSQL(String name) {
    return teamRepository.findByNameSQL(name);
  }

  public void updateTeamName(String id, String name) {
    Query query = new Query(Criteria.where("id").is(id));
    Update updateName = new Update().set("name", name);
    mongoTemplate.updateFirst(query, updateName, Team.class);
  }

  // Players in teams

  public Player getPlayer(String id) {
    log.info("getPlayer with id: " +id);
    return playerRepository.findById(id).orElse(null);
  }

  // Events

  public List<MatchEvent> getMatchEvents(String matchId) {
    return matchEventRepository.findByMatchId(matchId);
  }

  public List<MatchEvent> getPlayerEvents(String matchId, String playerId) {
    return matchEventRepository.findByMatchIdAndPlayerId(matchId, playerId);
  }

}
