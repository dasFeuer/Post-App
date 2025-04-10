package com.example.barun.repositories;

import com.example.barun.entities.reactionEntities.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    List<PostLike> findByPostId(Long postId);
    int countByPostId(Long postId);
    Optional<PostLike> findByUserAndPostId(Long userId, Long postId);
    boolean existsByUserAndPostId(Long userId, Long postId);
    void deleteByUserAndPostId(Long userId, Long postId);
}
