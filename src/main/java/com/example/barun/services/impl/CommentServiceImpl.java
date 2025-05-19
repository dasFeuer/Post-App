package com.example.barun.services.impl;

import com.example.barun.domain.CreateCommentRequest;
import com.example.barun.domain.UpdateCommentRequest;
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
    public Comments writeCommentOnUserPost(Long postId, CreateCommentRequest createCommentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> existingUser = userService.getUserByEmail(email);
        Optional<Post> existingPost = postService.getPostById(postId);

        if(existingUser.isPresent()) {
            if (existingPost.isPresent()) {
                Post thepost = existingPost.get();
                Comments newComment = new Comments();
                String comment = createCommentRequest.getComment();
                newComment.setComment(comment);
                newComment.setPost(thepost);
                newComment.setAuthor(existingUser.get());
                return commentRepository.save(newComment);
            } else {
                throw new EntityNotFoundException("Post not found!");
            }
        } else {
            throw new EntityNotFoundException("User not found!");
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
    public Comments updateComments(Long commentId, UpdateCommentRequest updateCommentRequest){
        Optional<Comments> existingComment = commentRepository.findById(commentId);
        if(existingComment.isPresent()){
            Comments updateComments = existingComment.get();
            String comment = updateCommentRequest.getComment();
            updateComments.setComment(comment);
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
