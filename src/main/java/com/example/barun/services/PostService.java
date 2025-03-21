package com.example.barun.services;

import com.example.barun.entities.postEntities.Post;
import com.example.barun.entities.postEntities.PostRequestDto;
import com.example.barun.entities.userEntities.User;
import com.example.barun.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    public Post createPostByUser(PostRequestDto postRequestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User author = userService.getUserByUsername(username);
        System.out.println("Username: " + username);

        if(!username.isEmpty()) {
            Post newPost = new Post();
            newPost.setTitle(postRequestDto.getTitle());
            newPost.setContent(postRequestDto.getContent());
            newPost.setAuthor(author);

            return postRepository.save(newPost);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

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
