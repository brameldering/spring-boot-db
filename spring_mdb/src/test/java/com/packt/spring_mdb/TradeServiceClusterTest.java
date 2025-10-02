package com.packt.spring_mdb;

import com.packt.spring_mdb.entities.Card;
import com.packt.spring_mdb.entities.User;
import com.packt.spring_mdb.service.TradingService;
import com.packt.spring_mdb.service.UserService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TradeServiceClusterTest extends InitTestForClusterTestContainer {

  @Autowired
  private UserService userService;
  @Autowired
  private TradingService tradingService;

  @Test
  void exchangeCard() {
    // ARRANGE
    User user1 = new User();
    user1.setUsername("user1");
    user1 = userService.createUser(user1);
    userService.buyTokens(user1.getId(), 10);
    userService.buyCards(user1.getId(), 10);
    List<Card> user1Cards = userService.getUserCards(user1.getId());

    User user2 = new User();
    user2.setUsername("user2");
    user2 = userService.createUser(user2);
    userService.buyTokens(user2.getId(), 10);
    userService.buyCards(user2.getId(), 1);
    List<Card> user2Cards = userService.getUserCards(user2.getId());

    // ACT
    Card card = user1Cards.getFirst();
    tradingService.exchangeCard(card.getId(), user2.getId(), 2);
    // ASSERT
    user1Cards = userService.getUserCards(user1.getId());
    assertTrue(user1Cards.stream().filter(c -> c.getId().equals(card.getId())).findFirst().isEmpty());
    user2Cards = userService.getUserCards(user2.getId());
    assertTrue(user2Cards.stream().filter(c -> c.getId().equals(card.getId())).findFirst().isPresent());

  }

  @Test
  void exchangeCard_detectConflict() throws InterruptedException, ExecutionException {
    // ARRANGE
    User user1 = new User();
    user1.setUsername("user1");
    user1 = userService.createUser(user1);
    userService.buyTokens(user1.getId(), 10000);
    userService.buyCards(user1.getId(), 1);
    List<Card> user1Cards = userService.getUserCards(user1.getId());

    User user2 = new User();
    user2.setUsername("user2");
    user2 = userService.createUser(user2);
    userService.buyTokens(user2.getId(), 10000);
    userService.buyCards(user2.getId(), 1);
    List<Card> user2Cards = userService.getUserCards(user2.getId());
    Card card = user1Cards.getFirst();

    final String user1Id = user1.getId(), user2Id = user2.getId();

    // ACT
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    ExecutorCompletionService<Boolean> executorCompletionService = new ExecutorCompletionService<>(executorService);
    for (int i = 0; i < 10; i++) {
      int finalI = i;
      executorCompletionService.submit(() -> {
        for(int j=0; j<100; j++) {
          if (!tradingService.exchangeCard(card.getId(), finalI%2==0 ? user2Id : user1Id, 1 )){
            return false;
          }
        }
        return true;
      });
    }

    List<Boolean> results = new ArrayList<>(10);
    for(int i=0; i<10; i++){
      results.add(executorCompletionService.take().get());
    }
    assertTrue(results.stream().anyMatch(r -> r.equals(false)));
  }

  @Test
  void exchangeCard_notEnoughTokens() {
    // ARRANGE
    User user1 = new User();
    user1.setUsername("user1");
    user1 = userService.createUser(user1);
    userService.buyTokens(user1.getId(), 10);
    userService.buyCards(user1.getId(), 10);
    List<Card> user1Cards = userService.getUserCards(user1.getId());

    User user2 = new User();
    user2.setUsername("user2");
    user2 = userService.createUser(user2);
    final String user2Id = user2.getId();


    Card card = user1Cards.getFirst();
    // ACT & ARRANGE
    assertThrows(RuntimeException.class, () -> tradingService.exchangeCard(card.getId(), user2Id, 2));
  }

}
