package com.example.barun.services;

import com.example.barun.domain.CreateCommentRequest;
import com.example.barun.domain.UpdateCommentRequest;
import com.example.barun.domain.entities.Comments;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comments writeCommentOnUserPost(Long postId, CreateCommentRequest createCommentRequest);
    Optional<Comments> getCommentsById(Long commentId);
    void deleteCommentById(Long commentId);
    Comments updateComments(Long commentId, UpdateCommentRequest updateCommentRequest);
    List<Comments> findAllComments();
    List<Comments> getCommentsByPostId(Long postId);
}
