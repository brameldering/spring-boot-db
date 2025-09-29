package com.packt.spring_mdb;

import com.packt.spring_mdb.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
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
import java.time.LocalDate;
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

  @Autowired
  org.springframework.data.mongodb.core.mapping.MongoMappingContext mappingContext;

  @Test
  void debugMapping() {
    MongoPersistentEntity<?> entity = mappingContext.getPersistentEntity(com.packt.spring_mdb.repository.Player.class);
    MongoPersistentProperty idProp = entity.getIdProperty();
    System.out.println("idProp: " + idProp);
    if (idProp != null) {
      System.out.println("  id property name: " + idProp.getName());
      System.out.println("  id mapped field:  " + idProp.getFieldName());
      System.out.println("  id type:          " + idProp.getActualType());
    }

    // Use the MongoPersistentProperty type explicitly to avoid overload ambiguity
    entity.doWithProperties((MongoPersistentProperty p) -> {
      System.out.println("property: " + p.getName() + " -> field: " + p.getFieldName() + " -> type: " + p.getActualType());
    });
  }

  // For debugging
  @Autowired
  PlayerRepository playerRepository;
  @Autowired
  org.springframework.data.mongodb.core.MongoTemplate mongoTemplate;
  @BeforeEach
  void seedPlayer() {
    playerRepository.save(new Player(
        "420334",
        3,
        "Eliana STABILE",
        "Defender",
        LocalDate.parse("1993-11-26"),
        167,
        61
    ));
  }

  @Test
  void debugFindPlayer() {
    // repository call
    var repoResult = playerRepository.findById("420334");
    System.out.println("playerRepository.findById -> " + repoResult);

    // direct template call (explicit _id)
    var templateById = mongoTemplate.findById("420334", Player.class, "players");
    System.out.println("mongoTemplate.findById -> " + templateById);

    // explicit query by _id as a Document
    var doc = mongoTemplate.getCollection("players")
        .find(new org.bson.Document("_id", "420334"))
        .first();
    System.out.println("raw collection find -> " + doc);

    // fail fast so CI shows output
    org.junit.jupiter.api.Assertions.assertNotNull(templateById, "mongoTemplate did not find the document");
  }

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
    Player player = footballService.getPlayer("420334");
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
