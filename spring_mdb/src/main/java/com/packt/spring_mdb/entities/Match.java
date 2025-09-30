package com.packt.spring_mdb.entities;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "matches")
public class Match {
  @Id
  private String id;

  private LocalDate matchDate;

  @Indexed
  @DBRef(lazy = false)
  private Team team1;

  @Indexed
  @DBRef(lazy = false)
  private Team team2;

  private Integer team1Goals;
  private Integer team2Goals;
}
