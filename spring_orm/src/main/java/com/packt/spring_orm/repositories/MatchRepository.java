package com.packt.spring_orm.repositories;

import java.util.List;

import com.packt.spring_orm.entities.MatchEntity;
import com.packt.spring_orm.entities.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchRepository extends JpaRepository<MatchEntity, Long>{
  @Query("SELECT p1 FROM MatchEntity m JOIN m.team1 t1 JOIN t1.players p1 WHERE m.id = ?1 UNION SELECT p2 FROM MatchEntity m JOIN m.team2 t2 JOIN t2.players p2 WHERE m.id = ?1")
  public List<PlayerEntity> findPlayersByMatchId(Long matchId);
}

