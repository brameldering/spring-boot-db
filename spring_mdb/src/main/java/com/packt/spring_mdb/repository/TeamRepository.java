package com.packt.spring_mdb.repository;

import com.packt.spring_mdb.entities.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends MongoRepository<Team,String> {

  // Teams

  Optional<Team> findByName(String teamName);

  List<Team> findByNameContaining(String teamName);

  @Query(value = "SELECT T FROM Team T WHERE T.name = ?0")
  List<Team> findByNameSQL(String name);

  // Players in teams

  @Query(value = "{'players._id': ?0}", fields = "{'players.$': 1}")
  Team findPlayerById(String id);

}
