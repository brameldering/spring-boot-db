package com.packt.spring_cassandra.controller;

import com.packt.spring_cassandra.entities.Comment;
import com.packt.spring_cassandra.model.CommentPost;
import com.packt.spring_cassandra.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
