package com.packt.spring_orm;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.NoSuchElementException;

import com.packt.spring_orm.entities.UserEntity;
import com.packt.spring_orm.records.User;
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
@ContextConfiguration(initializers = UserServiceTest.Initializer.class)
public class UserServiceTest {
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
  UserService userService;

  @Test
  public void testCreateUser() {
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("test");
    userEntity.setId(1L);

    User user = userService.createUser("test");

    assertEquals("test", user.username());
  }

  @Test
  public void testGetUsers() {
    userService.createUser("test1");
    userService.createUser("test2");
    List<User> users = userService.getUsers();
    assertEquals(2, users.size());
    assertEquals("test1", users.get(0).username());
    assertEquals("test2", users.get(1).username());
  }

  @Test
  public void testGetUser() {
    User user1 = userService.createUser("test1");

    User user = userService.getUserById(user1.id());

    assertEquals("test1", user.username());
    assertEquals(user1.id(), user.id());
    assertEquals(user1, user);
  }

  @Test
  public void testGetUserNotFound() {
    assertThrows(NoSuchElementException.class, () -> userService.getUserById(888888L));
  }
}
