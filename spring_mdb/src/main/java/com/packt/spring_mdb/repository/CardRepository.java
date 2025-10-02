package com.packt.spring_mdb.repository;

import com.packt.spring_mdb.entities.Card;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CardRepository extends MongoRepository<Card, String> {
  List<Card> findByOwnerId(String id);
}
