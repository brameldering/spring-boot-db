package com.packt.spring_orm.records;

import com.packt.spring_orm.entities.MatchEventDetail;

import java.time.LocalDateTime;

public record MatchEvent(LocalDateTime time, MatchEventDetail details) {
}
