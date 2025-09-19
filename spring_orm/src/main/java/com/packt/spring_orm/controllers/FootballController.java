package com.packt.spring_orm.controllers;

import com.packt.spring_orm.records.Player;
import com.packt.spring_orm.records.Team;
import com.packt.spring_orm.services.FootballService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/football")
public class FootballController {

  private final FootballService footballService;

  public FootballController(FootballService footballService) {
    this.footballService = footballService;
  }

  @GetMapping("/teams/{id}")
  public Team getTeam(@PathVariable Long id) {
    return footballService.getTeam(id);
  }

  @GetMapping("/players")
  public List<Player> searchPlayers(@RequestParam String name) {
    return footballService.searchPlayers(name);
  }

  @GetMapping("/players/birth/{date}")
  public List<Player> searchPlayersByBirthDate(@PathVariable LocalDate date) {
    return footballService.searchPlayersByDOB(date);
  }

  @PostMapping("/teams")
  public Team createTeam(@RequestBody String name) {
    return footballService.createTeam(name);
  }

  @PutMapping("/player/{id}/position")
  public Player updatePlayerPosition(@PathVariable Long id, @RequestBody String position) {
    return footballService.updatePlayerPosition(id, position);
  }

}
