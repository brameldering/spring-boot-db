package com.packt.spring_mdb;

import com.packt.spring_mdb.entities.Player;
import com.packt.spring_mdb.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PlayerTest extends InitFootballServiceTest {

  @Autowired
  protected PlayerService playerService;

  @Test
  void getPlayer() {
    Player player = playerService.getPlayer("420334");
    assertThat(player, notNullValue());
  }
}