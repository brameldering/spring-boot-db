package com.packt.spring_orm.controllers;

import java.util.List;

import com.packt.spring_orm.records.User;
import com.packt.spring_orm.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public List<User> getUsers() {
    return userService.getUsers();
  }

  @GetMapping("/{id}")
  public User getUser(Long id) {
    return userService.getUserById(id);
  }

  @PostMapping
  public User createUser(@RequestBody String name) {
    return userService.createUser(name);
  }
}
