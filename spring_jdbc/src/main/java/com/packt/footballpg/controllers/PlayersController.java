package com.packt.footballpg.controllers;

import com.packt.footballpg.records.Player;
import com.packt.footballpg.services.PlayersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayersController {

  @Autowired
  private PlayersService playersService;

  @GetMapping
  public List<Player> getPlayers() {
    return playersService.getPlayers();
  }

  @GetMapping("/{id}")
  public Player getPlayer(@PathVariable Long id) {
    return playersService.getPlayer(id);
  }

  @PostMapping
  public void createPlayer(@RequestBody Player player) {
     playersService.createPlayer(player);
  }
}
