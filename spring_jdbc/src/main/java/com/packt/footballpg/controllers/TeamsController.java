package com.packt.footballpg.controllers;

import com.packt.footballpg.records.Team;
import com.packt.footballpg.services.TeamsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamsController {

  private final TeamsService teamsService;

  public TeamsController(TeamsService teamsService) {
    this.teamsService = teamsService;
  }

  @GetMapping("/count")
  public int getTeamsCount() {
    return teamsService.getTeamsCount();
  }

  @GetMapping
  public List<Team> getTeams() {
    return teamsService.getTeamsExplicitly();
  }

  @GetMapping("/{id}")
  public Team getTeamById(@PathVariable Long id) {
    return teamsService.getTeamById(id);
  }
}
