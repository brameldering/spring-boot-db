package com.packt.spring_mdb.service;

import com.packt.spring_mdb.entities.Team;
import com.packt.spring_mdb.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
  private final TeamRepository teamRepository;
  private final MongoTemplate mongoTemplate;

  public TeamService(TeamRepository teamRepository, MongoTemplate mongoTemplate) {
    this.teamRepository = teamRepository;
    this.mongoTemplate = mongoTemplate;
  }

  private static Logger log = LoggerFactory.getLogger(TeamService.class);

  public Team getTeam(String id) {
    log.info("getTeam with id: " +id);
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
}
