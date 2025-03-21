package com.example.barun.controllers;

import com.example.barun.entities.postEntities.Post;
import com.example.barun.entities.postEntities.PostRequestDto;
import com.example.barun.services.PostService;
import com.example.barun.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostControllers {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping("/createPost")
    public ResponseEntity<Post> create(@RequestBody PostRequestDto postRequestDto){
        Post createdPost = postService.createPostByUser(postRequestDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/getAllPosts")
    public List<Post> findAllPost(){
        return postService.getAllBlogs();
    }

    @DeleteMapping("/{id}/delete")
    public void deletePostById(@PathVariable Long id){
        postService.deleteBlogById(id);
    }
}
