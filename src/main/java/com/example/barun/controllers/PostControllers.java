package com.example.barun.controllers;

import com.example.barun.entities.postEntities.Post;
import com.example.barun.entities.userEntities.User;
import com.example.barun.repositories.UserRepository;
import com.example.barun.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostControllers {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;


}
