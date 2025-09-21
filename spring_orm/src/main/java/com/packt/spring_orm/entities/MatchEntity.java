package com.packt.spring_orm.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="matches")
public class MatchEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private LocalDate matchDate;

  @ManyToOne
  @JoinColumn(name = "team1_id", nullable = false)
  private TeamEntity team1;

  @ManyToOne
  @JoinColumn(name = "team2_id", nullable = false)
  private TeamEntity team2;

  @Column(name = "team1_goals", columnDefinition = "integer default 0")
  private Integer team1Goals;

  @Column(name = "team2_goals", columnDefinition = "integer default 0")
  private Integer team2Goals;
}
