package com.packt.spring_cassandra.repository;

import com.packt.spring_cassandra.entities.Comment;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends CassandraRepository<Comment, String> {
  @AllowFiltering
  List<Comment> findByTargetTypeAndTargetId(String targetType, String targetId);

  List<Comment> findByDateBetween(LocalDateTime start, LocalDateTime end);

  @AllowFiltering
  List<Comment> findByLabelsContains(String label);

}
