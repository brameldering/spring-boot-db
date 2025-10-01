package com.packt.spring_mdb.repository;

import com.packt.spring_mdb.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}
