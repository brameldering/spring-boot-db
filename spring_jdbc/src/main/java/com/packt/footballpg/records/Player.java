package com.packt.footballpg.records;

import java.time.LocalDate;

public record Player(Long id, Integer jerseyNumber, String name, String position, LocalDate dateOfBirth, Long teamId) {
}
