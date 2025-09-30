package com.packt.spring_mdb.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "players")
public class Player {
  @Id
  private String id;
  private Integer jerseyNumber;
  private String name;
  private String position;
  private LocalDate dateOfBirth;
  private Integer height;
  private Integer weight;
}
