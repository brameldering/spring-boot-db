package com.packt.spring_orm;

import com.packt.spring_orm.records.Team;
import com.packt.spring_orm.services.FootballService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = FootballServiceTest.Initializer.class)
@ExtendWith(SpringExtension.class)
public class FootballServiceTest {

  @Container
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
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

  // The @BeforeAll method is not needed when using the @Container annotation
//  @BeforeAll
//  public static void startContainer() {
//    postgreSQLContainer.start();
//  }

  @Autowired
  private FootballService footballService;

  @Test
  public void testCreateTeam() {
    Team team = footballService.createTeam("Jamaica");

    assertThat(team, notNullValue());
    Team team2 = footballService.getTeam(team.id());
    assertThat(team2, notNullValue());
    assertThat(team.id(), is(team.id()));
  }

  @Test
  public void testGetTeams() {
    Team team = footballService.createTeam("Jamaica");
    assertThat(footballService.getTeam(team.id()), notNullValue());
  }

  @Test
  public void testGetTeam_notFound() {
    assertThat(footballService.getTeam(999999L), nullValue());
  }

}
