package com.example.barun.repositories;

import com.example.barun.domain.entities.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {

   List<Comments> findCommentsByPostId(Long postId);

}
