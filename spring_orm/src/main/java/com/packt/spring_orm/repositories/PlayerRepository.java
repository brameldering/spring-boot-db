package com.packt.spring_orm.repositories;

import com.packt.spring_orm.entities.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
  List<PlayerEntity> findByNameContainingIgnoreCase(String name);
  List<PlayerEntity> findByDateOfBirth(LocalDate dateOfBirth);
}
