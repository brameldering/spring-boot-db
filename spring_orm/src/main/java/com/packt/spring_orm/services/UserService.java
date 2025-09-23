package com.packt.spring_orm.services;

import com.packt.spring_orm.entities.UserEntity;
import com.packt.spring_orm.records.User;
import com.packt.spring_orm.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
  private UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return userRepository.findAll().stream().map(userEntity -> new User(userEntity.getId(), userEntity.getUsername())).collect(Collectors.toList());
  }

  public User getUserById(Long id) {
    return userRepository.findById(id).map(userEntity -> new User(userEntity.getId(), userEntity.getUsername())).orElseThrow();
  }

  public User createUser(String userName) {
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(userName);
    userRepository.save(userEntity);
    return new User(userEntity.getId(), userEntity.getUsername());
  }

}
