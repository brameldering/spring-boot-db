// src/test/java/com/packt/spring_mdb/AbstractMongoTest.java
package com.packt.spring_mdb;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
public abstract class AbstractMongoTest {

  // Define a static container for all tests to share (faster execution)
  // We use a simple mongo image here, as it doesn't need to be sharded just for the context test.
  @Container
  public static MongoDBContainer mongoDBContainer = new MongoDBContainer(
      DockerImageName.parse("mongo:latest")
  );

  // This method dynamically sets the MongoDB URI before the Spring context starts
  @DynamicPropertySource
  static void setMongoDbProperties(DynamicPropertyRegistry registry) {
    // Use the connection URI provided by Testcontainers
    registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
  }
}