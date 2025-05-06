package com.example.barun.controllers;

import com.example.barun.domain.dtos.CommentDto;
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

import java.io.IOException;
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

    private User getAuthotrizedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.getUserByUsername(username);
    }

    private ResponseEntity<?> unauthorizedUser(){
        return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/{postId}/writeComment")
    public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommentDto commentDto) throws IOException {
        User loggedInUser = getAuthotrizedUser();
        if(loggedInUser == null){
            return unauthorizedUser();
        }
        try{
            Comments comments = commentService.writeCommentOnUserPost(postId, commentDto);
            return new ResponseEntity<>(comments, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{commentId}/updateComment")
    public ResponseEntity<Comments> updateComment( @PathVariable Long commentId,
                                                   @RequestBody CommentDto commentDto){
        User loggedInUser = getAuthotrizedUser();
        if(loggedInUser == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Comments> getCommentsById = commentService.getCommentsById(commentId);
        if(getCommentsById.isPresent()){
            if (isCommentOwnedByUser(commentId, loggedInUser)) {
                Comments updatedComments = commentService.updateComments(commentId, commentDto);
                return new ResponseEntity<>(updatedComments, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @DeleteMapping("/deleteAllComment")
//    public void deleteAllComments(){
//         commentServiceImpl.deleteAllComments();
//    }

    @DeleteMapping("/{commentId}/deleteComment")
    public ResponseEntity<?> deleteCommentsById(@PathVariable Long commentId){
        User loggedInUser = getAuthotrizedUser();
        if(loggedInUser == null){
            return unauthorizedUser();
        }
        Optional<Comments> getCommentsById = commentService.getCommentsById(commentId);
        if(getCommentsById.isPresent()) {
            if(isCommentOwnedByUser(commentId, loggedInUser)){
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
//        User loggedInUser = getAuthorizedUser();
//        if(loggedInUser == null){
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//        Optional<Comments> commentsById = commentServiceImpl.getCommentsById(commentId);
//        if(commentsById.isPresent()){
//            if(isCommentOwnedByUser(commentId, loggedInUser)){
//                return ResponseEntity.ok(commentsById.get());
//            } else {
//                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//            }
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
        User loggedInUser = getAuthotrizedUser();
        if(loggedInUser == null){
            return unauthorizedUser();
        }
        List<Comments> comments = commentService.findAllComments();
        List<CommentDto> commentDto = comments.stream().map(comment -> commentMapper.toDto(comment)).toList();
         return ResponseEntity.ok(commentDto);
    }

    @GetMapping("/{postId}/postComments")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId){
        List<Comments> comments = commentService.getCommentsByPostId(postId);
        List<CommentDto> commentDto = comments.stream().map(comment -> commentMapper.toDto(comment)).toList();
        return ResponseEntity.ok(commentDto);

    }
}
