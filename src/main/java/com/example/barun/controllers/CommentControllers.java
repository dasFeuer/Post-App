package com.example.barun.controllers;

import com.example.barun.domain.CreateCommentRequest;
import com.example.barun.domain.UpdateCommentRequest;
import com.example.barun.domain.dtos.CommentDto;
import com.example.barun.domain.dtos.CreateCommentRequestDto;
import com.example.barun.domain.dtos.UpdateCommentRequestDto;
import com.example.barun.domain.entities.Comments;
import com.example.barun.domain.entities.User;
import com.example.barun.mappers.CommentMapper;
import com.example.barun.services.CommentService;
import com.example.barun.services.PostService;
import com.example.barun.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/postComments")
public class CommentControllers {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentMapper commentMapper;


    private boolean isCommentOwnedByUser(Long commentId, User loggedInUser){
        Optional<Comments> comments = commentService.getCommentsById(commentId);
        return comments.isPresent() && comments.get().getAuthor().getId().equals(loggedInUser.getId());
    }

    private Optional<User> getAuthorizedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email= authentication.getName();
        return userService.getUserByEmail(email);
    }

    private ResponseEntity<?> unauthorizedUser(){
        return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/{postId}/writeComment")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long postId, @RequestBody CreateCommentRequestDto createCommentRequestDto)  {
        Optional<User> loggedInUser = getAuthorizedUser();
        if(loggedInUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try{
            CreateCommentRequest createCommentRequest = commentMapper.toCreateCommentRequest(createCommentRequestDto);
            Comments comments = commentService.writeCommentOnUserPost(postId, createCommentRequest);
            CommentDto commentDto = commentMapper.toDto(comments);
            return new ResponseEntity<>(commentDto, HttpStatus.CREATED);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{commentId}/updateComment")
    public ResponseEntity<CommentDto> updateComment( @PathVariable Long commentId,
                                                   @RequestBody UpdateCommentRequestDto updateCommentRequestDto){
        Optional<User> loggedInUser = getAuthorizedUser();
        if(loggedInUser.isEmpty()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Comments> getCommentsById = commentService.getCommentsById(commentId);
        if(getCommentsById.isPresent()){
            if (isCommentOwnedByUser(commentId, loggedInUser.get())) {
                UpdateCommentRequest updateCommentRequest = commentMapper.toUpdateCommentRequest(updateCommentRequestDto);
                Comments updatedComments = commentService.updateComments(commentId, updateCommentRequest);
                CommentDto commentDto = commentMapper.toDto(updatedComments);
                return new ResponseEntity<>(commentDto, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{commentId}/deleteComment")
    public ResponseEntity<?> deleteCommentsById(@PathVariable Long commentId){
        Optional<User> loggedInUser = getAuthorizedUser();
        if(loggedInUser.isEmpty()){
            return unauthorizedUser();
        }
        Optional<Comments> getCommentsById = commentService.getCommentsById(commentId);
        if(getCommentsById.isPresent()) {
            if(isCommentOwnedByUser(commentId, loggedInUser.get())){
                commentService.deleteCommentById(commentId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{commentId}/comments")
    public ResponseEntity<?> getCommentsById(@PathVariable Long commentId){
        Optional<Comments> comments = commentService.getCommentsById(commentId);
        if(comments.isPresent()){
            CommentDto commentDto = commentMapper.toDto(comments.get());
            return ResponseEntity.ok(commentDto);
        } else {
            return new ResponseEntity<>("Comments not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/allComments")
    public ResponseEntity<?> findAllComments(){
        Optional<User> loggedInUser = getAuthorizedUser();
        if(loggedInUser.isEmpty()){
            return unauthorizedUser();
        }
        List<Comments> comments = commentService.findAllComments();
        List<CommentDto> commentDto = comments.stream().map(comment -> commentMapper.toDto(comment)).toList();
         return ResponseEntity.ok(commentDto);
    }

    @GetMapping("/{postId}/postComments")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId){
        List<Comments> comments = commentService.getCommentsByPostId(postId);
        List<CommentDto> commentDto = comments.stream().map(commentMapper::toDto).toList();
        return ResponseEntity.ok(commentDto);

    }
}
