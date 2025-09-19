package com.packt.spring_orm.repositories;

import com.packt.spring_orm.entities.TeamEntity;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<TeamEntity, Long> {
}
