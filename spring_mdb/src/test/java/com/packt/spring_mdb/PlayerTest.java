package com.packt.spring_mdb;

import com.packt.spring_mdb.entities.Player;
import com.packt.spring_mdb.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlayerTest extends InitFootballServiceTest {

  @Autowired
  protected PlayerService playerService;

  @Test
  void getPlayer() {
    Player player = playerService.getPlayer("420334");
    logger.info("player: {}", player);
    assertThat(player, notNullValue());
  }

  @Test
  void savePlayer() {
    // ARRANGE
    // Constructor fields based on your Player entity:
    // Player(String id, Integer jerseyNumber, String name, String position, LocalDate dateOfBirth, Integer height, Integer weight)

    Player playerToSave = new Player(
        null,                       // id (null for new creation)
        2,                          // jerseyNumber
        "Juno Test",                // name
        "Defender",                 // position
        LocalDate.of(1995, 8, 15),  // dateOfBirth
        185,                        // height (in cm)
        80                          // weight (in kg)
    );

    // ACT
    Player savedPlayer = playerService.savePlayer(playerToSave);

    // ASSERT
    // 1. Check the returned object and its generated ID
    assertNotNull(savedPlayer, "The saved player object should not be null.");
    logger.info("savedPlayer id: " + savedPlayer.getId());
    assertThat("The saved player should have a generated ID.", savedPlayer.getId(), notNullValue());

    // 2. Verify some key data points were saved correctly
    assertThat(savedPlayer.getName(), is("Juno Test"));
    assertThat(savedPlayer.getJerseyNumber(), is(2));

    // 3. (Best practice) Retrieve and verify persistence
    Player retrievedPlayer = playerService.getPlayer(savedPlayer.getId());
    assertThat("The player should be retrievable by its new ID.", retrievedPlayer, notNullValue());
    assertThat(retrievedPlayer.getPosition(), is("Defender"));

    // CLEANUP (Crucial for a clean test environment)
    // Ensure you have this method in your PlayerService: playerRepository.deleteById(id);
    playerService.deletePlayer(savedPlayer.getId());
  }
}