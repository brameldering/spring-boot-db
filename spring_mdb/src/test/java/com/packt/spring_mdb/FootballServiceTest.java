package com.packt.spring_mdb;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import com.packt.spring_mdb.service.FootballService;
import com.packt.spring_mdb.repository.Player;
import com.packt.spring_mdb.repository.Team;

@SpringBootTest
@Testcontainers
public class FootballServiceTest {

  static Logger logger = LoggerFactory.getLogger(FootballServiceTest.class);

  static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo")
      .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/teams.json"), "teams.json");

  @BeforeAll
  static void startContainer() throws IOException, InterruptedException {
    logger.info("Before MongoDBContainer start and file imported");
    mongoDBContainer.start();
    importFile("teams");
    logger.info("After MongoDBContainer start and file imported");
  }

  static void importFile(String fileName) throws IOException, InterruptedException {
    Container.ExecResult res = mongoDBContainer.execInContainer("mongoimport", "--db=football", "--collection=" + fileName, "--jsonArray", fileName + ".json");
    if (res.getExitCode() > 0){
      throw new RuntimeException("MongoDB not properly initialized");
    }
  }

  @DynamicPropertySource
  static void setMongoDbProperties(DynamicPropertyRegistry registry) {
    logger.info("Before registry.add");
    registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    logger.info("After registry.add");
  }

  @Autowired
  private FootballService footballService;

  @Test
  void getTeam() {
    logger.info("Before getTeam test");
    Team team = footballService.getTeam("1884881");
    logger.info("After getTeam test");
    assertNotNull(team);
    logger.info("After assertNotNull getTeam test");

  }

  @Test
  void getTeam_notExists() {
    Team team = footballService.getTeam("99999999");
    assertNull(team);
  }

  @Test
  void getTeamByName() {
    Team team = footballService.getTeamByName("Argentina");
    assertNotNull(team);
  }

  @Test
  void getTeamsContainingName() {
    List<Team> teams = footballService.getTeamsContainingName("land");
    assertNotNull(teams);
    assertThat(teams, not(empty()));
    assertThat(teams, hasSize(5));
  }

  @Test
  void getPlayer() {
    Player player = footballService.getPlayer("387138");
    assertThat(player, notNullValue());
  }

  @Test
  void saveTeam() {
    // ACT
    Team t = new Team();
    t.setName("Senegal");
    Team savedTeam = footballService.saveTeam(t);
    // ASSERT
    assertThat(savedTeam, notNullValue());
    assertThat(savedTeam.getId(), notNullValue());
    Team retreivedTeam = footballService.getTeam(savedTeam.getId());
    assertThat(retreivedTeam, notNullValue());
    // CLEAN-UP
    footballService.deleteTeam(retreivedTeam.getId());
  }

  @Test
  void deleteTeam() {
    // ARRANGE
    Team t = new Team();
    t.setName("Senegal");
    Team savedTeam = footballService.saveTeam(t);
    // ACT
    footballService.deleteTeam(savedTeam.getId());
    // ASSERT
    Team deletedTeam = footballService.getTeam(savedTeam.getId());
    assertThat(deletedTeam, nullValue());
  }

  @Test
  void updateTeamName() {
    // ARRANGE
    Team t = new Team();
    t.setName("Veneçuela");
    Team savedTeam = footballService.saveTeam(t);
    // ACT
    footballService.updateTeamName(savedTeam.getId(), "Venezuela");
    // ASSERT
    Team updatedTeam = footballService.getTeam(savedTeam.getId());
    assertThat(updatedTeam.getName(), is("Venezuela"));
  }

}
