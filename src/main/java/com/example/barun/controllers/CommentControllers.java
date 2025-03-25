package com.example.barun.controllers;

import com.example.barun.dto.CommentDto;
import com.example.barun.entities.commentEntities.Comments;
import com.example.barun.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/postComments")
public class CommentControllers {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{postId}/writeComment")
    public Comments createComment(@PathVariable Long postId, @RequestBody CommentDto commentDto) throws IOException {
        return commentService.writeCommentOnUserPost(postId, commentDto);
    }
}
