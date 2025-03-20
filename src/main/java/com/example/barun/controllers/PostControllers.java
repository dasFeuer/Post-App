package com.example.barun.controllers;

import com.example.barun.entities.postEntities.Post;
import com.example.barun.entities.userEntities.User;
import com.example.barun.repositories.UserRepository;
import com.example.barun.services.PostService;
import com.example.barun.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostControllers {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;


    @GetMapping("/getAllPosts")
    public List<Post> findAllPost(){
        return postService.getAllBlogs();
    }

    @DeleteMapping("/{id}/delete")
    public void deletePostById(@PathVariable Long id){
        postService.deleteBlogById(id);
    }
}
