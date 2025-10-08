package com.packt.spring_cassandra.controller;

import com.packt.spring_cassandra.entities.Comment;
import com.packt.spring_cassandra.model.CommentPost;
import com.packt.spring_cassandra.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/comment")
public class CommentController {
  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @PostMapping
  public Comment postComment(@RequestBody CommentPost commentPost) {
    return commentService.postComment(commentPost);
  }

  @GetMapping
  public List<Comment> getComments() {
    return commentService.getComments();
  }

  @GetMapping("/{targetType}/{targetId}")
  public List<Comment> getComment(@PathVariable String targetType, @PathVariable String targetId) {
    return commentService.getComments(targetType, targetId);
  }

  @GetMapping("{targetType}/{targetId}/search")
  public List<Comment> getComments (
      @PathVariable String targetType,
      @PathVariable String targetId,
      @RequestParam Optional<String> userId,
      @RequestParam Optional<LocalDateTime> start,
      @RequestParam Optional<LocalDateTime> end,
      @RequestParam Optional<Set<String>> label) {
    return commentService.getComments(targetType, targetId, userId, start, end, label);
  }

  @GetMapping("{targetType}/{targetId}/searchstr")
  public List<Comment> getCommentsString(
      @PathVariable String targetType,
      @PathVariable String targetId,
      @RequestParam Optional<String> userId,
      @RequestParam Optional<LocalDateTime> start,
      @RequestParam Optional<LocalDateTime> end,
      @RequestParam Optional<Set<String>> label) {
    return commentService.getCommentsString(targetType, targetId, userId, start, end, label);
  }
}
