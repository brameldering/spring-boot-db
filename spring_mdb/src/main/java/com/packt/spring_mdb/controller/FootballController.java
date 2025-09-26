package com.packt.spring_mdb.controller;

import com.packt.spring_mdb.repository.Player;
import com.packt.spring_mdb.repository.Team;
import com.packt.spring_mdb.service.FootballService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/football")
@RestController
public class FootballController {
  private final FootballService footballService;

  public FootballController(FootballService footballService) {
    this.footballService = footballService;
  }

  @GetMapping("/team/{id}")
  public Team getTeam(@PathVariable String id) {
    return footballService.getTeam(id);
  }

  @GetMapping("/team")
  public Team getTeamByName(@RequestParam String name) {
    return footballService.getTeamByName(name);
  }

  @GetMapping("/team/{name}/sql")
  public List<Team> getTeamByNameSQL(@PathVariable String name) {
    return footballService.getTeamByNameSQL(name);
  }

  @GetMapping("/team/{name}/contains")
  public List<Team> getTeamsContainingName(@PathVariable String name) {
    return footballService.getTeamsContainingName(name);
  }

  @PostMapping("/team")
  public Team saveTeam(@RequestBody Team team) {
    return footballService.saveTeam(team);
  }

  @DeleteMapping("/team/{id}")
  public void deleteTeam(@PathVariable String id) {
    footballService.deleteTeam(id);
  }

  @PatchMapping("/team/{id}")
  public void updateTeamName(@PathVariable String id, @RequestParam String name) {
    footballService.updateTeamName(id, name);
  }

  @GetMapping("/player/{id}")
  public Player getPlayer(@PathVariable String id) {
    return footballService.getPlayer(id);
  }

}
