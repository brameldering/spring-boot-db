package com.packt.spring_cassandra.service;

import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.packt.spring_cassandra.entities.Comment;
import com.packt.spring_cassandra.model.CommentPost;
import com.packt.spring_cassandra.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.EntityWriteResult;
import org.springframework.data.cassandra.core.UpdateOptions;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.CriteriaDefinition;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CommentService {

  private final CommentRepository commentRepository;

  private final CassandraTemplate cassandraTemplate;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public CommentService(CommentRepository commentRepository, CassandraTemplate cassandraTemplate) {
    this.commentRepository = commentRepository;
    this.cassandraTemplate = cassandraTemplate;
  }


  public List<Comment> getComments() {
    return commentRepository.findAll();
  }

  public List<Comment> getComments (String targetType, String targetId) {
    return commentRepository.findByTargetTypeAndTargetId(targetType, targetId);
  }

  public List<Comment> getComments(String targetType, String targetId, Optional<String> userId, Optional<LocalDateTime> start, Optional<LocalDateTime> end, Optional<Set<String>> labels) {
    Select select = QueryBuilder.selectFrom("comment").all().whereColumn("targetType").isEqualTo(QueryBuilder.literal(targetType)).whereColumn("targetId").isEqualTo(QueryBuilder.literal(targetId));

    if (userId.isPresent()) {
      select = select.whereColumn("userId").isEqualTo(QueryBuilder.literal(userId.get()));
    }

    if (start.isPresent()) {
      select = select.whereColumn("date").isGreaterThan(QueryBuilder.literal(start.get().toString()));
    }

    if (end.isPresent()) {
      select = select.whereColumn("date").isLessThan(QueryBuilder.literal(end.get().toString()));
    }

    if (labels.isPresent()) {
      for (String label : labels.get()) {
        select = select.whereColumn("labels").contains(QueryBuilder.literal(label));
      }
    }

    return cassandraTemplate.select(select.allowFiltering().build(), Comment.class);
  }

  public List<Comment> getCommentsString(String targetType, String targetId, Optional<String> userId,
                                         Optional<LocalDateTime> start, Optional<LocalDateTime> end, Optional<Set<String>> labels) {
    String query = "SELECT * FROM comment WHERE targetType ='" + targetType + "' AND targetId='" + targetId + "'";
    if (userId.isPresent()) {
      query += " AND userId='" + userId.get() + "'";
    }
    if (start.isPresent()) {
      query += " AND date > '" + start.get() + "'";
    }
    if (end.isPresent()) {
      query += " AND date < '" + end.get() + "'";
    }
    if (labels.isPresent()) {
      for (String label : labels.get()) {
        query += " AND labels CONTAINS '" + label + "'";
      }
    }

    query += " ALLOW FILTERING";
    return cassandraTemplate.select(query, Comment.class);
  }

  public Comment postComment(CommentPost commentPost) {
    Comment comment = new Comment();
    comment.setCommentId(UUID.randomUUID().toString());
    comment.setUserId(commentPost.userId());
    comment.setTargetType(commentPost.targetType());
    comment.setTargetId(commentPost.targetId());
    comment.setContent(commentPost.commentContent());
    comment.setDate(LocalDateTime.now());
    comment.setLabels(commentPost.labels());
    return commentRepository.save(comment);
  }

  public Comment upvoteComment(String commentId) {
    int MAX_RETRIES = 3;
    Random rnd = new Random();
    for (int i = 0; i < MAX_RETRIES; i++) {
      logger.info("Upvoting comment with id: " + commentId + " attempt: " + i);
      Comment comment = commentRepository.findByCommentId(commentId).orElse(null);
      logger.info("Fetched comment: " + comment);
      if (comment != null) {
        Integer currentVotes = comment.getUpvotes();
        logger.info("Current votes: " + currentVotes);
        EntityWriteResult<Comment> result;

        if (currentVotes == null) {
          // First upvote: LWT with upvotes IS NULL
          comment.setUpvotes(1);
          logger.info("Using LWT IF upvotes IS NULL");
          CriteriaDefinition ifNull = Criteria.where("upvotes").is(null);
          UpdateOptions optionsNull = UpdateOptions.builder().ifCondition(ifNull).build();
          result = cassandraTemplate.update(comment, optionsNull);
        } else {
          // Normal upvote: LWT with upvotes = currentVotes
          comment.setUpvotes(currentVotes + 1);
          logger.info("Using LWT IF upvotes = " + currentVotes);
          comment.setUpvotes(currentVotes + 1);
          CriteriaDefinition ifEqual = Criteria.where("upvotes").is(currentVotes);
          UpdateOptions optionsEqual = UpdateOptions.builder().ifCondition(ifEqual).build();
          result = cassandraTemplate.update(comment, optionsEqual);
        }
        if (result.wasApplied()) {
          return result.getEntity();
        }

        // Backoff before retrying
        try {
          Thread.sleep(rnd.nextInt(5, 100));
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      } else {
        throw new IllegalStateException("Comment " + commentId + " not found.");
      }
    }
    throw new IllegalStateException("comment " + commentId
        + " was updated concurrently and could not be upvoted. Wait a few moments and try again.");
  }
}
