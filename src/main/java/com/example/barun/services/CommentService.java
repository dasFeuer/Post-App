package com.example.barun.services;

import com.example.barun.domain.dtos.CommentDto;
import com.example.barun.domain.entities.Comments;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comments writeCommentOnUserPost(Long postId, CommentDto commentDto);
    Optional<Comments> getCommentsById(Long commentId);
    void deleteCommentById(Long commentId);
    Comments updateComments(Long commentId, CommentDto commentDto);
    List<Comments> findAllComments();
    List<Comments> getCommentsByPostId(Long postId);
}
