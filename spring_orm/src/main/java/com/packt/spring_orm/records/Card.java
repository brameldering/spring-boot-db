package com.packt.spring_orm.records;

import java.util.Optional;

public record Card(Long id, Long ownerId, Optional<Long> albumId, Player player) {
}
