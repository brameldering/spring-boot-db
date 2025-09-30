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

}
