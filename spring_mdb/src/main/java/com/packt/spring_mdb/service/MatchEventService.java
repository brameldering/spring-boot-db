package com.packt.spring_mdb.service;

import com.packt.spring_mdb.entities.MatchEvent;
import com.packt.spring_mdb.repository.MatchEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchEventService {
  private final MatchEventRepository matchEventRepository;

  public MatchEventService(MongoTemplate mongoTemplate, MatchEventRepository matchEventRepository) {
    this.matchEventRepository = matchEventRepository;
  }

  private static Logger log = LoggerFactory.getLogger(MatchEventService.class);

  public List<MatchEvent> getMatchEvents(String matchId) {
    return matchEventRepository.findByMatchId(matchId);
  }

  public List<MatchEvent> getPlayerEvents(String matchId, String playerId) {
    return matchEventRepository.findByMatchIdAndPlayerId(matchId, playerId);
  }

}
