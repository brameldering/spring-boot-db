package com.packt.spring_orm.repositories;

import java.util.List;

import com.packt.spring_orm.entities.AlbumEntity;
import com.packt.spring_orm.entities.PlayerEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {

  @Query("SELECT p FROM PlayerEntity p WHERE p NOT IN (SELECT c.player FROM CardEntity c WHERE c.album.id=:id)")
  public List<PlayerEntity> findByIdMissingPlayers(Long id);

  @Query("SELECT p FROM PlayerEntity p JOIN p.cards c WHERE c.album.id = :id")
  public List<PlayerEntity> findByIdPlayers(Long id, Pageable page);

  @Query("SELECT p FROM PlayerEntity p JOIN p.cards c WHERE c.album.id = :id AND p.team.id = :teamId")
  public List<PlayerEntity> findByIdAndTeam(Long id, Long teamId);

}
