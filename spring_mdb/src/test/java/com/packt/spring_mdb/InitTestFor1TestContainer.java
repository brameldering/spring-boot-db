package com.packt.spring_mdb;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;

@SpringBootTest
@Testcontainers
public abstract class InitTestFor1TestContainer {

  static Logger logger = LoggerFactory.getLogger(InitTestFor1TestContainer.class);

  // Initialize and configure the MongoDB container with data files
  static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo")
      .withSharding()
      .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/teams.json"), "teams.json")
      .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/players.json"), "players.json")
      .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/matches.json"), "matches.json")
      .withCopyFileToContainer(MountableFile.forClasspathResource("mongo/match_events.json"), "match_events.json");

  @BeforeAll
  static void startContainer() throws IOException, InterruptedException {
    mongoDBContainer.start();
    // Load data into MongoDB before any test runs
    importFile(mongoDBContainer, "matches");
    importFile(mongoDBContainer, "match_events");
    importFile(mongoDBContainer, "teams");
    importFile(mongoDBContainer, "players");
  }

  // Utility method to import JSON data using mongoimport
  static void importFile(MongoDBContainer container, String fileName) throws IOException, InterruptedException {
    String uri = "mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000";
    Container.ExecResult res = container.execInContainer("mongoimport", "--uri=" + uri, "--db=football", "--collection=" + fileName, "--jsonArray", fileName + ".json");
    if (res.getExitCode() > 0) {
      throw new RuntimeException("MongoDB not properly initialized");
    }
  }

  // Dynamic property source to configure Spring Data MongoDB URI
  @DynamicPropertySource
  static void setMongoDbProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", () -> mongoDBContainer.getReplicaSetUrl("football"));
  }

}
