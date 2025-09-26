package com.packt.spring_mdb.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
  private String id;
  private Integer jerseyNumber;
  private String name;
  private String position;
  private LocalDate dateOfBirth;
  private Integer height;
  private Integer weight;
}
