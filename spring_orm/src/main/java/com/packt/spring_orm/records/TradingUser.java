package com.packt.spring_orm.records;

import java.util.List;

public record TradingUser(User user, List<Card> cards, List<Album> albums) {
}
