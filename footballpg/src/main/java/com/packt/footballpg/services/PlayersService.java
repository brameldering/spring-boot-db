package com.packt.footballpg.services;

import com.packt.footballpg.entities.Player;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayersService {
  private final JdbcClient jdbcClient;

  public PlayersService(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public List<Player> getPlayers() {
    return jdbcClient.sql("SELECT * FROM players").query(Player.class).list();
  }

  public Player getPlayer(Long id) {
    return jdbcClient.sql("SELECT * FROM players WHERE id = :id").param("id", id).query(Player.class).single();
  }

  public void createPlayer(Player player) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcClient.sql("""
            INSERT INTO players (jersey_number, name, position, date_of_birth, team_id) VALUES (:jersey_number, :name, :position, :date_of_birth, :team_id)
            """).param("jersey_number", player.jerseyNumber())
                .param("name", player.name())
                .param("position", player.position())
        .param("date_of_birth", player.dateOfBirth())
        .param("team_id", player.teamId())
        .update(keyHolder, "id");

        // Potentially extend with returning a new object corresponding to created player
        // update(keyHolder, "id");
        // createdPlayer.setId(KeyHolder.getKey().intValue());
        // return createdPlayer;
  }

}
