package com.packt.spring_mdb.service;

import com.mongodb.client.result.UpdateResult;
import com.packt.spring_mdb.controller.UserController;
import com.packt.spring_mdb.entities.Card;
import com.packt.spring_mdb.entities.Player;
import com.packt.spring_mdb.entities.User;
import com.packt.spring_mdb.repository.CardRepository;
import com.packt.spring_mdb.repository.PlayerRepository;
import com.packt.spring_mdb.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final CardRepository cardRepository;
  private final PlayerRepository playerRepository;
  private final MongoTemplate mongoTemplate;

  private final Logger log = LoggerFactory.getLogger(UserController.class);

  public UserService(UserRepository userRepository, CardRepository cardRepository, PlayerRepository playerRepository, MongoTemplate mongoTemplate) {
    this.userRepository = userRepository;
    this.cardRepository = cardRepository;
    this.playerRepository = playerRepository;
    this.mongoTemplate = mongoTemplate;
  }

  public Integer buyTokens(String userId, Integer tokens) {
    log.info("buyTokens called");
    Query query = new Query(Criteria.where("id").is(userId));
    Update update = new Update().inc("tokens", tokens);
    log.info("query: {}", query);
    UpdateResult result = mongoTemplate.updateFirst(query, update, User.class, "users");
    log.info("result: {}", result);
    return (int) result.getModifiedCount();
  }

  @Transactional
  public Integer buyCards(String userId, Integer count) {
    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isPresent()) {
      User user = userOpt.get();
      List<Player> availablePlayers = getAvailablePlayers();
      Random random = new Random();
      if (user.getTokens() >= count) {
        user.setTokens(user.getTokens() - count);
      } else {
        throw new RuntimeException("Not enough tokens");
      }
      List<Card> cards = Stream.generate(() -> {
        Card card = new Card();
        card.setOwner(user);
        card.setPlayer(availablePlayers.get(random.nextInt(0, availablePlayers.size())));
        return card;
      }).limit(count).toList();
      List<Card> savedCards = cardRepository.saveAll(cards);
      userRepository.save(user);
      return savedCards.size();
    }
    return 0;
  }

  @Cacheable(value = "availablePlayers")
  public List<Player> getAvailablePlayers() {
    return playerRepository.findAll();
  }

  public User createUser(User user) {
    return userRepository.save(user);
  }

  public Optional<User> getUser(String id){
    return userRepository.findById(id);
  }

  public List<Card> getUserCards(String id){
    return cardRepository.findByOwnerId(id);
  }

}
