package com.example.barun.services.impl;

import com.example.barun.domain.dtos.CommentDto;
import com.example.barun.domain.entities.Comments;
import com.example.barun.domain.entities.Post;
import com.example.barun.domain.entities.User;
import com.example.barun.repositories.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostServiceImpl postServiceImpl;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Transactional
    public Comments writeCommentOnUserPost(Long postId, CommentDto commentDto) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User existingUser = userServiceImpl.getUserByUsername(username);
        Optional<Post> existingPost = postServiceImpl.getPostById(postId);

        if(existingPost.isPresent()) {
            Post thepost = existingPost.get();
            Comments newComment = new Comments();
            newComment.setComment(commentDto.getComment());
            newComment.setPost(thepost);
            newComment.setAuthor(existingUser);
            return commentRepository.save(newComment);
        } else {
            throw new IOException("Post not found!");
        }
    }

    public Optional<Comments> getCommentsById(Long commentId){
        return commentRepository.findById(commentId);
    }

    public void deleteCommentById(Long commentId){
        if(commentRepository.existsById(commentId)){
            commentRepository.deleteById(commentId);
        }
    }

//    public void deleteAllComments(){
//        commentRepository.deleteAll();
//    }

    public Comments updateComments(Long commentId, CommentDto commentDto){
        Optional<Comments> existingComment = commentRepository.findById(commentId);
        if(existingComment.isPresent()){
            Comments updateComments = existingComment.get();
            updateComments.setComment(commentDto.getComment());
            commentRepository.save(updateComments);
        }
        return existingComment.orElseThrow();
    }

    public List<Comments> findAllComments() {
        return commentRepository.findAll();
    }

    public List<Comments> getCommentsByPostId(Long postId){
        if(postServiceImpl.getPostById(postId).isEmpty()){
            throw new RuntimeException("Post not found");
        }
        return commentRepository.findCommentsByPostId(postId);
    }
}
