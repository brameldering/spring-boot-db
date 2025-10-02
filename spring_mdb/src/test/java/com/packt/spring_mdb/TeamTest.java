package com.packt.spring_mdb;

import com.packt.spring_mdb.service.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.packt.spring_mdb.entities.Team;

public class TeamTest extends InitTestFor1TestContainer {

  @Autowired
  protected TeamService teamService;

  @Test
  void getTeam() {
    Team team = teamService.getTeam("1884881");
    assertNotNull(team);
  }

  @Test
  void getTeam_notExists() {
    Team team = teamService.getTeam("99999999");
    assertNull(team);
  }

  @Test
  void getTeamByName() {
    Team team = teamService.getTeamByName("Argentina");
    assertNotNull(team);
  }

  @Test
  void getTeamsContainingName() {
    List<Team> teams = teamService.getTeamsContainingName("land");
    assertNotNull(teams);
    assertThat(teams, not(empty()));
    assertThat(teams, hasSize(5));
  }

  @Test
  void saveTeam() {
    // ACT
    Team t = new Team();
    t.setName("Senegal");
    Team savedTeam = teamService.saveTeam(t);
    // ASSERT
    assertThat(savedTeam, notNullValue());
    assertThat(savedTeam.getId(), notNullValue());
    Team retreivedTeam = teamService.getTeam(savedTeam.getId());
    assertThat(retreivedTeam, notNullValue());
    // CLEAN-UP
    teamService.deleteTeam(retreivedTeam.getId());
  }

  @Test
  void deleteTeam() {
    // ARRANGE
    Team t = new Team();
    t.setName("Senegal");
    Team savedTeam = teamService.saveTeam(t);
    // ACT
    teamService.deleteTeam(savedTeam.getId());
    // ASSERT
    Team deletedTeam = teamService.getTeam(savedTeam.getId());
    assertThat(deletedTeam, nullValue());
  }

  @Test
  void updateTeamName() {
    // ARRANGE
    Team t = new Team();
    t.setName("Veneçuela");
    Team savedTeam = teamService.saveTeam(t);
    // ACT
    teamService.updateTeamName(savedTeam.getId(), "Venezuela");
    // ASSERT
    Team updatedTeam = teamService.getTeam(savedTeam.getId());
    assertThat(updatedTeam.getName(), is("Venezuela"));
  }

}
