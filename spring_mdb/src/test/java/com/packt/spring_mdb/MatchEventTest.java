package com.packt.spring_mdb;

import com.packt.spring_mdb.entities.MatchEvent;
import com.packt.spring_mdb.service.MatchEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class MatchEventTest extends InitTestFor1TestContainer {

  @Autowired
  protected MatchEventService matchEventService;

  @Test
  void getMatchEvents() {
    List<MatchEvent> events = matchEventService.getMatchEvents("400222852");
    assertThat(events, not(empty()));
  }

  @Test
  void getPlayerEvents() {
    List<MatchEvent> playerEvents = matchEventService.getPlayerEvents("400222844", "413022");
    assertThat(playerEvents, not(empty()));
  }
}