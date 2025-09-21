package com.packt.spring_orm.controllers;

import com.packt.spring_orm.records.Player;
import com.packt.spring_orm.records.Team;
import com.packt.spring_orm.repositories.TeamPlayers;
import com.packt.spring_orm.services.FootballService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/football")
public class FootballController {

  private final FootballService footballService;

  public FootballController(FootballService footballService) {
    this.footballService = footballService;
  }

  @GetMapping("/teams")
  public List<Team> getTeams() {
    return footballService.getAllTeams();
  }

  @GetMapping("/teams/{id}")
  public Team getTeam(@PathVariable Long id) {
    return footballService.getTeam(id);
  }

  @GetMapping("/teams/{id}/players")
  public List<Player> getTeamPlayers(@PathVariable Long id) {
    return footballService.getTeamPlayers(id);
  }

  @PostMapping("/teams")
  public Team createTeam(@RequestBody String name) {
    return footballService.createTeam(name);
  }

  @GetMapping("/players")
  public List<Player> searchPlayers(@RequestParam String name) {
    return footballService.searchPlayers(name);
  }

  @GetMapping("/players/birth/{date}")
  public List<Player> searchPlayersByBirthDate(@PathVariable LocalDate date) {
    return footballService.searchPlayersByDOB(date);
  }

  @PutMapping("/player/{id}/position")
  public Player updatePlayerPosition(@PathVariable Long id, @RequestBody String position) {
    return footballService.updatePlayerPosition(id, position);
  }

  @GetMapping("/matches/{id}/players")
  public List<Player> getPlayersByMatch(@PathVariable Long id) {
    return footballService.getPlayersByMatch(id);
  }

  @GetMapping("/albums/{id}/{teamId}/players")
  public List<Player> getAlbumTeamPlayers(@PathVariable Long id, @PathVariable Long teamId) {
    return footballService.getAlbumPlayersByTeam(id, teamId);
  }

  @GetMapping("/albums/{id}/missingplayers")
  public List<Player> getAlbumMissingPlayers(@PathVariable Long id) {
    return footballService.getAlbumMissingPlayers(id);
  }

  @GetMapping("/albums/{id}/myplayers")
  public List<Player> getAlbumMyPlayers(@PathVariable Long id) {
    return footballService.getAlbumPlayers(id);
  }

  @GetMapping("/players/list")
  public List<Player> getPlayersList(@RequestParam List<Long> players) {
    return footballService.getPlayersList(players);
  }

  @GetMapping("/players/startwith")
  public List<Player> searchPlayersStartingWith(@RequestParam String startingName) {
    return footballService.searchPlayersStartingWith(startingName);
  }

  @GetMapping("/players/search")
  public List<Player> searchPlayersLike(@RequestParam String q) {
    return footballService.searchPlayersLike(q);
  }

  @GetMapping("/players/paginated")
  public List<Player> getPlayers(@RequestParam Map<String, String> params) {
    Integer page = Integer.parseInt(params.getOrDefault("page", "0"));
    Integer size = Integer.parseInt(params.getOrDefault("size", "10"));
    return footballService.getAllPlayersPaged(page, size);
  }

  @GetMapping("/teams/{position}/count")
  public List<TeamPlayers> getNumberOfPlayersByPosition(@PathVariable String position) {
    return footballService.getNumberOfPlayersByPosition(position);
  }

}
