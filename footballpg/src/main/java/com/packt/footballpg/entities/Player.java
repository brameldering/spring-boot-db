package com.packt.footballpg.entities;

import java.time.LocalDate;

public record Player(Integer id, Integer jerseyNumber, String name, String position, LocalDate dateOfBirth, Integer teamId) {
}
