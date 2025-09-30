package com.packt.spring_mdb.repository;

import com.packt.spring_mdb.entities.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match, String> {

}
