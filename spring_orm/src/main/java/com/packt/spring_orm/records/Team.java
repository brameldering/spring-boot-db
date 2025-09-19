package com.packt.spring_orm.records;

import java.util.List;

public record Team(Long id, String name, List<Player> players) {}
