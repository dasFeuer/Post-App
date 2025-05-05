package com.example.barun.services.impl;

import com.example.barun.domain.dtos.CommentDto;
import com.example.barun.domain.entities.Comments;
import com.example.barun.domain.entities.Post;
import com.example.barun.domain.entities.User;
import com.example.barun.repositories.CommentRepository;
import com.example.barun.services.CommentService;
import com.example.barun.services.PostService;
import com.example.barun.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public Comments writeCommentOnUserPost(Long postId, CommentDto commentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User existingUser = userService.getUserByUsername(username);
        Optional<Post> existingPost = postService.getPostById(postId);

        if(existingPost.isPresent()) {
            Post thepost = existingPost.get();
            Comments newComment = new Comments();
            newComment.setComment(commentDto.getComment());
            newComment.setPost(thepost);
            newComment.setAuthor(existingUser);
            return commentRepository.save(newComment);
        } else {
            throw new EntityNotFoundException("Post not found!");
        }
    }

    @Override
    public Optional<Comments> getCommentsById(Long commentId){
        return commentRepository.findById(commentId);
    }

    @Override
    public void deleteCommentById(Long commentId){
        if(commentRepository.existsById(commentId)){
            commentRepository.deleteById(commentId);
        }
    }

//    public void deleteAllComments(){
//        commentRepository.deleteAll();
//    }

    @Override
    public Comments updateComments(Long commentId, CommentDto commentDto){
        Optional<Comments> existingComment = commentRepository.findById(commentId);
        if(existingComment.isPresent()){
            Comments updateComments = existingComment.get();
            updateComments.setComment(commentDto.getComment());
            commentRepository.save(updateComments);
        }
        return existingComment.orElseThrow();
    }

    @Override
    public List<Comments> findAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comments> getCommentsByPostId(Long postId){
        if(postService.getPostById(postId).isEmpty()){
            throw new RuntimeException("Post not found");
        }
        return commentRepository.findCommentsByPostId(postId);
    }
}
