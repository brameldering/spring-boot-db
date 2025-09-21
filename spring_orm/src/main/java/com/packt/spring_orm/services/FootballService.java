package com.packt.spring_orm.services;

import com.packt.spring_orm.entities.PlayerEntity;
import com.packt.spring_orm.entities.TeamEntity;
import com.packt.spring_orm.records.Player;
import com.packt.spring_orm.records.Team;
import com.packt.spring_orm.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class FootballService {
  private final PlayerRepository playerRepository;
  private final TeamRepository teamRepository;
  private MatchRepository matchRepository;
  private AlbumRepository albumRepository;

  public FootballService(PlayerRepository playerRepository, TeamRepository teamRepository,MatchRepository matchRepository, AlbumRepository albumRepository) {
    this.playerRepository = playerRepository;
    this.teamRepository = teamRepository;
    this.matchRepository = matchRepository;
    this.albumRepository = albumRepository;
  }

  public List<Player> searchPlayers(String name) {
    return playerRepository.findByNameContainingIgnoreCase(name).stream()
        .map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(), player.getDateOfBirth())).toList();
  }

  public List<Player> searchPlayersStartingWith(String nameStarting) {
    return playerRepository.findByNameStartingWith(nameStarting)
        .stream()
        .map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(),
            player.getDateOfBirth()))
        .toList();
  }

  public List<Player> searchPlayersByDOB(LocalDate dateOfBirth) {
    return playerRepository.findByDateOfBirth(dateOfBirth).stream().map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(), player.getDateOfBirth())).toList();
  }

//  public List<Team> getTeams() {
//    return teamRepository.getTeams().stream().map(team -> new Team(team.getId(),team.getName() LIST).toList();
//  }

//  @Transactional(readOnly = true)
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

  public List<Player> getPlayersByMatch(Long id) {
    return matchRepository.findPlayersByMatchId(id)
        .stream()
        .map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(),
            player.getDateOfBirth()))
        .toList();
  }

  public List<Player> getAlbumMissingPlayers(Long id) {
    return albumRepository.findByIdMissingPlayers(id)
        .stream()
        .map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(),
            player.getDateOfBirth()))
        .toList();
  }

  public List<Player> getAlbumPlayers(Long id) {
    return albumRepository.findByIdPlayers(id, Pageable.ofSize(10).withPage(0))
        .stream()
        .map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(),
            player.getDateOfBirth()))
        .toList();
  }

  public List<Player> getAlbumPlayersByTeam(Long albumId, Long teamId) {
    return albumRepository.findByIdAndTeam(albumId, teamId)
        .stream()
        .map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(),
            player.getDateOfBirth()))
        .toList();
  }

  public List<Player> getPlayersList(List<Long> players) {
    // return playerRepository.findListOfPlayers(players)
    return playerRepository.findByIdIn(players)
        .stream()
        .map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(),
            player.getDateOfBirth()))
        .toList();
  }

  public List<Player> searchPlayersLike(String q) {
    return playerRepository.findByNameLike("%" + q + "%")
        .stream()
        .map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(),
            player.getDateOfBirth()))
        .toList();
  }

  public List<Player> getTeamPlayers(Long id) {
    return playerRepository.findByTeamId(id, Sort.by("name").ascending()).stream()
        .map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(),
            player.getDateOfBirth()))
        .toList();
  }

  public List<Player> getAllPlayersPaged(int pageNumber, int size) {
    Page<PlayerEntity> page = playerRepository.findAll(Pageable.ofSize(size).withPage(pageNumber));
    return page.stream()
        .map(player -> new Player(player.getName(), player.getJerseyNumber(), player.getPosition(),
            player.getDateOfBirth()))
        .toList();
  }

  public List<TeamPlayers> getNumberOfPlayersByPosition(String position) {
    return teamRepository.getNumberOfPlayersByPosition(position);
  }

}
