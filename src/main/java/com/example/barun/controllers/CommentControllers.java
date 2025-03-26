package com.example.barun.controllers;

import com.example.barun.dto.CommentDto;
import com.example.barun.entities.commentEntities.Comments;
import com.example.barun.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/postComments")
public class CommentControllers {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{postId}/writeComment")
    public Comments createComment(@PathVariable Long postId, @RequestBody CommentDto commentDto) throws IOException {
        return commentService.writeCommentOnUserPost(postId, commentDto);
    }

    @PutMapping("/{commentId}/updateComment")
    public Comments updateComment(@PathVariable Long commentId, @RequestBody CommentDto commentDto){
        return commentService.updateComments(commentId, commentDto);
    }

    @DeleteMapping("/deleteAllComment")
    public void deleteAllComments(){
         commentService.deleteAllComments();
    }

    @DeleteMapping("/{commentId}/deleteComment")
    public void deleteCommentsById(@PathVariable Long commentId){
         commentService.deleteCommentById(commentId);
    }

    @GetMapping("/{commentId}/comments")
    public Optional<Comments> findCommentsById(@PathVariable Long commentId){
         return commentService.getCommentsById(commentId);
    }

    @GetMapping("/allComments")
    public List<Comments> findAllComments(){
         return commentService.findAllComments();
    }


}
