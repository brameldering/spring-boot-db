package com.packt.spring_mdb.repository;

import com.packt.spring_mdb.entities.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, String> {

}
