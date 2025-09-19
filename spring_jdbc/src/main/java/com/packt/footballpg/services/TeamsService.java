package com.packt.footballpg.services;

import com.packt.footballpg.records.Team;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamsService {
  private final JdbcTemplate jdbcTemplate;

  public TeamsService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public int getTeamsCount() {
    return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM teams", Integer.class))
        .orElse(0);
  }

  public Team getTeamById(int id) {
    return jdbcTemplate.queryForObject("SELECT * FROM teams WHERE id = ?", (rs, rowNum) -> new Team(rs.getInt("id"), rs.getString("name")), id);
  }

//  public List<Team> getAllTeams() {
//    return jdbcTemplate.query("SELECT * FROM teams ORDER BY name", new BeanPropertyRowMapper<>(Team.class));
//  }

  public List<Team> getTeamsExplicitly() {
    return jdbcTemplate.query("SELECT * FROM teams ORDER BY name", (rs, rowNum) -> new Team(rs.getInt("id"), rs.getString("name")));
   }

}
