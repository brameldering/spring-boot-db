package com.packt.spring_mdb.service;

import com.packt.spring_mdb.entities.Player;
import com.packt.spring_mdb.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
  private final PlayerRepository playerRepository;

  public PlayerService(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  private static Logger log = LoggerFactory.getLogger(PlayerService.class);

  public Player getPlayer(String id) {
    log.info("getPlayer with id: " +id);
    return playerRepository.findById(id).orElse(null);
  }

  /**
   * Saves a new Player entity to the database.
   * If the player object has a non-null ID, it updates the existing player.
   * If the player object has a null ID, it creates a new player.
   * @param player The Player object to save.
   * @return The saved Player object, potentially with a generated ID.
   */
  public Player savePlayer(Player player) {
    log.info("savePlayer: " + player.toString()); // Assuming Player has a toString()
    return playerRepository.save(player);
  }

  public void deletePlayer(String id) {
    log.info("deletePlayer with id: " +id);
    playerRepository.deleteById(id);
  }
}
