package com.packt.spring_mdb.controller;

import com.packt.spring_mdb.entities.MatchEvent;
import com.packt.spring_mdb.entities.Player;
import com.packt.spring_mdb.entities.Team;
import com.packt.spring_mdb.service.MatchEventService;
import com.packt.spring_mdb.service.PlayerService;
import com.packt.spring_mdb.service.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/football")
@RestController
public class FootballController {
  private final TeamService teamService;
  private final PlayerService playerService;
  private final MatchEventService matchEventService;

  public FootballController(TeamService teamService,  PlayerService playerService, MatchEventService matchEventService) {
    this.teamService = teamService;
    this.playerService = playerService;
    this.matchEventService = matchEventService;
  }

  @GetMapping("/team/{id}")
  public Team getTeam(@PathVariable String id) {
    return teamService.getTeam(id);
  }

  @GetMapping("/team")
  public Team getTeamByName(@RequestParam String name) {
    return teamService.getTeamByName(name);
  }

  @GetMapping("/team/{name}/sql")
  public List<Team> getTeamByNameSQL(@PathVariable String name) {
    return teamService.getTeamByNameSQL(name);
  }

  @GetMapping("/team/{name}/contains")
  public List<Team> getTeamsContainingName(@PathVariable String name) {
    return teamService.getTeamsContainingName(name);
  }

  @PostMapping("/team")
  public Team saveTeam(@RequestBody Team team) {
    return teamService.saveTeam(team);
  }

  @DeleteMapping("/team/{id}")
  public void deleteTeam(@PathVariable String id) {
    teamService.deleteTeam(id);
  }

  @PatchMapping("/team/{id}")
  public void updateTeamName(@PathVariable String id, @RequestParam String name) {
    teamService.updateTeamName(id, name);
  }

  // Players
  @GetMapping("/player/{id}")
  public Player getPlayer(@PathVariable String id) {
    return playerService.getPlayer(id);
  }

  // Events
  @GetMapping("/match/{id}/events")
  public List<MatchEvent> getMatchEvents(@PathVariable String id) {
    return matchEventService.getMatchEvents(id);
  }

  @GetMapping("/match/{matchId}/{playerId}/events")
  public List<MatchEvent> getPlayerEvents(@PathVariable String matchId, @PathVariable String playerId) {
    return matchEventService.getPlayerEvents(matchId, playerId);
  }

}
