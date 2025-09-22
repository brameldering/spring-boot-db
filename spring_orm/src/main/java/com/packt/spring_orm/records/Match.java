package com.packt.spring_orm.records;

import java.time.LocalDate;
import java.util.List;

public record Match(Long id, String team1, String team2, Integer team1Goals, Integer team2Goals, LocalDate matchDate, List<MatchEvent> events) {
}
