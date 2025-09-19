package com.packt.spring_orm.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "teams")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
  private List<PlayerEntity> players;

}
