package com.packt.spring_cassandra.service;

import com.packt.spring_cassandra.entities.Comment;
import com.packt.spring_cassandra.model.CommentPost;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.cassandra.CassandraContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest
public class CommentServiceTest {

  static CassandraContainer cassandraContainer = (CassandraContainer) new CassandraContainer("cassandra").withInitScript("createKeyspace.cql").withExposedPorts(9042);

  @BeforeAll
  static void startContainer() throws IOException, InterruptedException {
    cassandraContainer.start();
  }

  @DynamicPropertySource
  static void setCassandraProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.cassandra.keyspace-name", () -> "footballKeyspace");
    registry.add("spring.cassandra.contact-points", () -> cassandraContainer.getContactPoint().getAddress());
    registry.add("spring.cassandra.port", () -> cassandraContainer.getMappedPort(9042));
    registry.add("spring.cassandra.local-datacenter", () -> cassandraContainer.getLocalDatacenter());
  }

  @Autowired
  private CommentService commentService;

  @Test
  void postCommentTest() {
    CommentPost commentPost = new CommentPost("user1", "player", "1", "The best!", Set.of("label1", "label2"));
    Comment result = commentService.postComment(commentPost);
    assertNotNull(result);
    assertNotNull(result.getCommentId());
  }

}
