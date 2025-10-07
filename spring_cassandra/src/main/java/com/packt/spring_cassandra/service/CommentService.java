package com.packt.spring_cassandra.service;

import com.packt.spring_cassandra.entities.Comment;
import com.packt.spring_cassandra.model.CommentPost;
import com.packt.spring_cassandra.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

  private final CommentRepository commentRepository;

  public CommentService(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  public List<Comment> getComments (String targetType, String targetId) {
    return commentRepository.findByTargetTypeAndTargetId(targetType, targetId);
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

  public List<Comment> getComments() {
    return commentRepository.findAll();
  }

}
