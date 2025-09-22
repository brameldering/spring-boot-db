package com.packt.spring_orm;

import com.packt.spring_orm.records.MatchEvent;
import com.packt.spring_orm.records.Player;
import com.packt.spring_orm.records.Team;
import com.packt.spring_orm.repositories.TeamPlayers;
import com.packt.spring_orm.services.FootballService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = FootballServiceTest.Initializer.class)
@ExtendWith(SpringExtension.class)
public class FootballServiceTest {

  @Container
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

  @Test
  public void getPlayers() {
    List<Player> players = footballService.searchPlayers("Adriana");
    assertThat(players, not(empty()));
  }

  @Test
  public void getPlayersByMatch() {
    List<Player> players = footballService.getPlayersByMatch(400222852L);
    assertThat(players, not(empty()));
  }

  @Test
  public void getPlayersByMatch_notFound() {
    List<Player> players = footballService.getPlayersByMatch(9999999L);
    assertThat(players, empty());
  }

  @Test
  public void getPlayersByBirthDate() {
    List<Player> players = footballService.searchPlayersByDOB(LocalDate.of(1994, 2, 4));
    assertThat(players, not(empty()));
  }

  @Test
  public void getAlbumMissingPlayers() {
    List<Player> players = footballService.getAlbumMissingPlayers(1L);
    assertThat(players, not(empty()));
  }

  @Test
  public void getAlbumPlayers() {
    List<Player> players = footballService.getAlbumPlayers(1L);
    assertThat(players, not(empty()));
  }

  @Test
  public void getAlbumPlayersByTeam() {
    List<Player> players = footballService.getAlbumPlayersByTeam(1L, 1884881L);
    assertThat(players, not(empty()));
  }

  @Test
  public void getPlayersList() {
    List<Player> players = footballService.getPlayersList(List.of(357669L, 420326L, 420324L));
    assertThat(players, not(empty()));
    assertThat(players, hasSize(3));
  }

  @Test
  public void searchPlayersLike() {
    List<Player> players = footballService.searchPlayersLike("Adriana");
    assertThat(players, not(empty()));
  }

  @Test
  public void getTeamPlayers() {
    List<Player> players = footballService.getTeamPlayers(1884823L);
    assertThat(players, not(empty()));
  }

  @Test
  public void getTeamPlayers_notFound() {
    List<Player> players = footballService.getTeamPlayers(9999999L);
    assertThat(players, empty());
  }

  @Test
  public void getAllPlayersPaged() {
    List<Player> players = footballService.getAllPlayersPaged(0, 10);
    assertThat(players, not(empty()));
    assertThat(players, hasSize(10));
  }

  @Test
  public void getNumberOfPlayersByPosition() {
    List<TeamPlayers> players = footballService.getNumberOfPlayersByPosition("Midfielder");
    assertThat(players, not(empty()));
  }

  @Test
  public void getMatchEventsOfType() {
    List<MatchEvent> events = footballService.getMatchEventsOfType(400222852L, 24);
    assertThat(events, not(empty()));
  }

  @Test
  public void getMatchEventsOfType_notFound() {
    List<MatchEvent> events = footballService.getMatchEventsOfType(9999999L, 24);
    assertThat(events, empty());
  }

  @Test
  public void getMatchWithPlayerEvents() {
    List<MatchEvent> events = footballService.getMatchWithPlayerEvents(400222852L, 358189L);
    assertThat(events, not(empty()));
  }

  @Test
  public void getMatchWithPlayerEvents_notFound() {
    List<MatchEvent> events = footballService.getMatchWithPlayerEvents(9999999L, 358189L);
    assertThat(events, empty());
  }

  @Test
  public void getMatchWithPlayerEventsError() {
    assertThrows(InvalidDataAccessResourceUsageException.class,
        () -> footballService.getMatchWithPlayerEventsError(400222852L, 358189L));
  }


}
