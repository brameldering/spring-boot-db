package com.packt.spring_orm.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cards", uniqueConstraints = {@UniqueConstraint(columnNames = {"album_id", "player_id"}) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "album_id")
  private AlbumEntity album;

  @ManyToOne
  @JoinColumn(name = "player_id")
  private PlayerEntity player;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  private UserEntity owner;

}
