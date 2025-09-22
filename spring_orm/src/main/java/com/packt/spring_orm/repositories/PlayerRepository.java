package com.packt.spring_orm.repositories;

import com.packt.spring_orm.entities.PlayerEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.time.LocalDate;
import java.util.List;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
  List<PlayerEntity> findByNameContainingIgnoreCase(String name);
  List<PlayerEntity> findByDateOfBirth(LocalDate dateOfBirth);

  @Query("SELECT p FROM PlayerEntity p WHERE p.id IN (?1)")
  List<PlayerEntity> findListOfPlayers(List<Long> players);

  List<PlayerEntity> findByIdIn(List<Long> players);

  List<PlayerEntity> findByNameLike(String name);

  List<PlayerEntity> findByNameStartingWith(String name);

  List<PlayerEntity> findByTeamId(Long teamId, Sort sort);

  @Procedure("FIND_PLAYERS_WITH_MORE_THAN_N_MATCHES")
  int getTotalPlayersWithMoreThanNMatches(int num_matches);
}
