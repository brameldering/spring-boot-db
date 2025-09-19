package com.packt.spring_orm.services;

import com.packt.spring_orm.entities.PlayerEntity;
import com.packt.spring_orm.entities.TeamEntity;
import com.packt.spring_orm.records.Player;
import com.packt.spring_orm.records.Team;
import com.packt.spring_orm.repositories.PlayerRepository;
import com.packt.spring_orm.repositories.TeamRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FootballService {
  private final PlayerRepository playerRepository;
  private final TeamRepository teamRepository;

  public FootballService(PlayerRepository playerRepository, TeamRepository teamRepository) {
    this.playerRepository = playerRepository;
    this.teamRepository = teamRepository;
  }

  public List<Player> searchPlayers(String name) {
    return playerRepository.findByNameContainingIgnoreCase(name).stream()
        .map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(), player.getDateOfBirth())).toList();
  }

  public List<Player> searchPlayersByDOB(LocalDate dateOfBirth) {
    return playerRepository.findByDateOfBirth(dateOfBirth).stream().map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(), player.getDateOfBirth())).toList();
  }

  @Transactional(readOnly = true)
  public Team getTeam(Long id) {
    TeamEntity teamEntity = teamRepository.findById(id).orElse(null);
    if (teamEntity == null) {
      return null;
    } else {
      return new Team(teamEntity.getId(), teamEntity.getName(), teamEntity.getPlayers().stream().map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(), player.getDateOfBirth())).toList());
    }
  }

  public Team createTeam(String name) {
    TeamEntity teamEntity = new TeamEntity();
    teamEntity.setName(name);
    teamEntity = teamRepository.save(teamEntity);
    return new Team(teamEntity.getId(), teamEntity.getName(), List.of());
  }

  public Player updatePlayerPosition(Long id, String position) {
    PlayerEntity playerEntity = playerRepository.findById(id).orElse(null);
    if (playerEntity == null) {
      return null;
    }  else {
      playerEntity.setPosition(position);
      playerEntity = playerRepository.save(playerEntity);
      return new Player(playerEntity.getName(), playerEntity.getJerseyNumber(), playerEntity.getPosition(), playerEntity.getDateOfBirth());
    }
  }
}
