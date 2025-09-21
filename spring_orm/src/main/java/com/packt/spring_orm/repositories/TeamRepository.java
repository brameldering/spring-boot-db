package com.packt.spring_orm.repositories;

import com.packt.spring_orm.entities.TeamEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends CrudRepository<TeamEntity, Long> {
  @Query("SELECT DISTINCT t FROM TeamEntity t LEFT JOIN FETCH t.players")
  List<TeamEntity> findAllWithPlayers();

  @Query("SELECT t FROM TeamEntity t JOIN FETCH t.players WHERE t.id = ?1")
  public Optional<TeamEntity> findByIdWithPlayers(Integer id);

  @Query("SELECT p.team.name as name, count(p.id) as playersCount FROM PlayerEntity p WHERE p.position = ?1 GROUP BY p.team ORDER BY playersCount DESC")
  public List<TeamPlayers> getNumberOfPlayersByPosition(String position);
}
