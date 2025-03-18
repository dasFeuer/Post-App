package com.example.barun.controllers;

import com.example.barun.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostControllers {

    @Autowired
    private PostService postService;



}
