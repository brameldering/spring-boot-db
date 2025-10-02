package com.packt.spring_mdb;

import com.packt.spring_mdb.entities.Card;
import com.packt.spring_mdb.entities.User;
import com.packt.spring_mdb.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

public class UserServiceClusterTest extends InitTestForClusterTestContainer {

  @Autowired
  private UserService userService;

  @Test
  void buyTokens() {
    // ARRANGE
    User user = new User();
    user.setUsername("Sample user");
    User createdUser = userService.createUser(user);
    // ACT
    Integer numModified = userService.buyTokens(createdUser.getId(), 10);
    // ASSERT
    assertThat(numModified, is(1));
  }

  @Test
  void buyCards() {
    // ARRANGE
    User user = new User();
    user.setUsername("Sample user");
    User createdUser = userService.createUser(user);
    Integer buyTokens = 10;
    userService.buyTokens(createdUser.getId(), buyTokens);
    Integer requestedCards = 1;
    // ACT
    Integer cardCount = userService.buyCards(user.getId(), requestedCards);
    // ASSERT
    assertThat(cardCount, is(requestedCards));
    List<Card> cards = userService.getUserCards(user.getId());
    assertThat(cards, not(empty()));
    assertThat(cards, hasSize(requestedCards));
    Optional<User> updatedUser = userService.getUser(user.getId());
    assertThat(updatedUser.isPresent(), is(true) );
    assertThat(updatedUser.get().getTokens(), is(buyTokens-requestedCards));
  }

  @Test
  void buyCards_notEnoughTokens() {
    // ARRANGE
    User user = new User();
    user.setUsername("Sample user");
    User createdUser = userService.createUser(user);
    Integer buyTokens = 10;
    userService.buyTokens(createdUser.getId(), 10);
    Integer requestedCards = buyTokens + 1;
    // ACT & ASSERT
    assertThrows(RuntimeException.class, () -> userService.buyCards(user.getId(), requestedCards));
    Optional<User> actualUser = userService.getUser(createdUser.getId());
    assertThat(actualUser, notNullValue());
    assertThat(actualUser.get().getTokens(), is(buyTokens));
    List<Card> cards = userService.getUserCards(createdUser.getId());
    assertThat(cards, is(empty()));
  }

  @Test
  void createUser() {
    // ARRANGE
    User user = new User();
    user.setUsername("Sample user");
    // ACT
    User createdUser = userService.createUser(user);
    // ASSERT
    assertThat(createdUser, notNullValue());
    assertThat(createdUser.getId(), notNullValue());
  }

}
