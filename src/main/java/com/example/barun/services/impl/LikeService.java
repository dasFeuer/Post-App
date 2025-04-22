package com.example.barun.services.impl;

import com.example.barun.domain.entities.Post;
import com.example.barun.domain.entities.PostLike;
import com.example.barun.domain.entities.User;
import com.example.barun.repositories.PostLikeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    private PostLikeRepository likeRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private PostService postService;

    @Transactional
    public boolean toggleLike(Long postId) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loggedInUser = userServiceImpl.getUserByUsername(username);

        if(loggedInUser == null){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        Optional<Post> optionalPost = postService.getPostById(postId);
        if(optionalPost.isEmpty()){
            throw new IOException("Post not found with Id: " + postId);
        }

        Post post = optionalPost.get();

        // Checking if a user had already liked the post
        Optional<PostLike> existingLike = likeRepository.findByUserIdAndPostId(loggedInUser.getId(), postId);

        // If the like exists by user then remove it i.e. unlike
        if(existingLike.isPresent()){
            likeRepository.delete(existingLike.get());
            return false; // -- Post Was UnLike
        } else {
            // If the like doesn't exist by a user, then create it (like)
            PostLike newLike = new PostLike(loggedInUser, post);
            likeRepository.save(newLike);
            return true; // Post was liked
        }
    }

    public boolean hasUserLikedThePost(Long userId, Long postId){
        return likeRepository.existsByUserIdAndPostId(userId, postId);
    }

    public int getLikeCountForPost(Long postId){
        return likeRepository.countByPostId(postId);
    }

    public List<PostLike> getLikesForPost(Long postId){
        return likeRepository.findByPostId(postId);
    }
}
