package com.packt.spring_mdb.service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import com.packt.spring_mdb.repository.Player;
import com.packt.spring_mdb.repository.Team;
import com.packt.spring_mdb.repository.TeamRepository;

@Service
public class FootballService {
  private final TeamRepository teamRepository;
  private final MongoTemplate mongoTemplate;

  public FootballService(TeamRepository teamRepository, MongoTemplate mongoTemplate) {
    this.teamRepository = teamRepository;
    this.mongoTemplate = mongoTemplate;
  }

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
    Team team = teamRepository.findPlayerById(id);
    if (team != null) {
      return team.getPlayers().isEmpty() ? null : team.getPlayers().getFirst();
    } else {
      return null;
    }
  }



}
