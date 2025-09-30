package com.packt.spring_mdb.entities;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Sharded;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Sharded(shardKey = { "match" })
@Document(collection = "match_events")
public class MatchEvent {
  @Id
  private String id;

  @Field(name = "event_time")
  private LocalDateTime time;

  private Integer type;

  private String description;

  @Indexed
  @DBRef
  private Player player1;

  @Indexed
  @DBRef
  private Player player2;

  private List<String> mediaFiles;

  @Indexed
  @DBRef
  private Match match;
}
