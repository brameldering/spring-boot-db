package com.packt.spring_orm.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Table(name="players")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Integer jerseyNumber;
  private String name;
  private String position;
  private LocalDate dateOfBirth;
  private Integer height;
  private Integer weight;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private TeamEntity team;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
  private List<CardEntity> cards;
}
