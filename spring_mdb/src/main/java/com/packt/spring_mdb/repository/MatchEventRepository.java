package com.packt.spring_mdb.repository;

import java.util.List;

import com.packt.spring_mdb.entities.MatchEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MatchEventRepository extends MongoRepository<MatchEvent, String>{

    @Query(value = "{'match.$id': ?0}")
    List<MatchEvent> findByMatchId(String matchId);

    @Query(value = "{'$and': [{'match.$id': ?0}, {'$or':[ {'player1.$id':?1}, {'player2.$id':?1} ]}]}")
    List<MatchEvent> findByMatchIdAndPlayerId(String matchId, String playerId);
    
}
