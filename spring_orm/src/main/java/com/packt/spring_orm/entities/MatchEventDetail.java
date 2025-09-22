package com.packt.spring_orm.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchEventDetail {
  private Integer type;
  private String description;
  private List<Integer> players;
  private List<String> mediaFiles;
}
