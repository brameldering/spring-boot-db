package com.packt.spring_mdb.controller;

import com.packt.spring_mdb.entities.User;
import com.packt.spring_mdb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  private final Logger log = LoggerFactory.getLogger(UserController.class);

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/{id}/buyTokens")
  public Integer buyTokens(@PathVariable String id, @RequestBody Integer token) {
    log.info("Received request to buy tokens for user {}", id);
    return userService.buyTokens(id, token);
  }

  @PostMapping("/{id}/buyCards")
  public Integer buyCards(@PathVariable String id, @RequestBody Integer count) {
    return userService.buyCards(id, count);
  }

  @PostMapping
  public User createUser(@RequestBody User user) {
    return userService.createUser(user);
  }

}
