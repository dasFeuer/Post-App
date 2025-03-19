package com.example.barun.services;

import com.example.barun.entities.postEntities.Post;
import com.example.barun.entities.userEntities.User;
import com.example.barun.repositories.PostRepository;
import com.example.barun.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    public Optional<Post> getBlogById(Long id){
        return postRepository.findById(id);
    }

    public List<Post> getAllBlogs(){
        return postRepository.findAll();
    }

    public void deleteBlogById(Long id){
        postRepository.deleteById(id);
    }


}
