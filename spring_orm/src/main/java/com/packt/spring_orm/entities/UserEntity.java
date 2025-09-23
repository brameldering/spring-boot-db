package com.packt.spring_orm.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  @OneToMany(mappedBy = "owner")
  private List<CardEntity> ownedCards;

  @OneToMany(mappedBy = "owner")
  private Set<AlbumEntity> ownedAlbums;
}
