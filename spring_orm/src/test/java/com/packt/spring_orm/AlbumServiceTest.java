package com.packt.spring_orm;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.packt.spring_orm.records.Album;
import com.packt.spring_orm.records.Card;
import com.packt.spring_orm.records.User;
import com.packt.spring_orm.services.AlbumService;
import com.packt.spring_orm.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = AlbumServiceTest.Initializer.class)
public class AlbumServiceTest {

  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.8")
      .withDatabaseName("football")
      .withUsername("football")
      .withPassword("football");

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues.of(
              "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
              "spring.datasource.username=" + postgreSQLContainer.getUsername(),
              "spring.datasource.password=" + postgreSQLContainer.getPassword())
          .applyTo(configurableApplicationContext.getEnvironment());
    }
  }

  @BeforeAll
  public static void startContainer() {
    postgreSQLContainer.start();
  }

  @Autowired
  AlbumService albumService;
  @Autowired
  UserService userService;

  @Test
  public void testBuyAlbum() {

    // ARRANGE
    User user = userService.createUser("Test");
    Album album = albumService.buyAlbum(user.id(), "Sample album");

    assertEquals("Sample album", album.title());
    assertEquals(user.id(), album.ownerId());

  }

  @Test
  public void testBuyCards() {
    User user = userService.createUser("Test");
    List<Card> cards = albumService.buyCards(user.id(), 5);
    assertThat(cards, hasSize(5));
  }

  @Test
  public void addCardToAlbum() {
    // ARRANGE
    User user = userService.createUser("Test");
    Album album1 = albumService.buyAlbum(user.id(), "sample album");
    List<Card> cards = albumService.buyCards(user.id(), 5);

    // ACT
    Card card = albumService.addCardToAlbum(cards.get(0).id(), album1.id());
    assertThat(card.albumId(), notNullValue());
  }

  @Test
  public void useAllCardAvailable() {
    // ARRANGE
    User user = userService.createUser("Test");
    Album album1 = albumService.buyAlbum(user.id(), "sample album");
    List<Card> cards = albumService.buyCards(user.id(), 5);

    // ACT
    List<Card> usedCards = albumService.useAllCardAvailable(user.id());
    assertThat(usedCards, hasSize(5));
    for (Card card : usedCards) {
      assertThat(card.albumId(), notNullValue());
    }
  }
}
