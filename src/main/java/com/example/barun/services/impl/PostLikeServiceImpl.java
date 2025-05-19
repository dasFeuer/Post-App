package com.example.barun.services.impl;

import com.example.barun.domain.entities.Post;
import com.example.barun.domain.entities.PostLike;
import com.example.barun.domain.entities.User;
import com.example.barun.repositories.PostLikeRepository;
import com.example.barun.services.PostLikeService;
import com.example.barun.services.PostService;
import com.example.barun.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostLikeServiceImpl implements PostLikeService {
    @Autowired
    private PostLikeRepository likeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Transactional
    @Override
    public boolean toggleLike(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> loggedInUser = userService.getUserByEmail(email);

        if(loggedInUser.isEmpty()){
            throw new UsernameNotFoundException("User not found with username: " + email);
        }

        Optional<Post> optionalPost = postService.getPostById(postId);
        if(optionalPost.isEmpty()){
            throw new EntityNotFoundException("Post not found with Id: " + postId);
        }

        Post post = optionalPost.get();

        // Checking if a user had already liked the post
        Optional<PostLike> existingLike = likeRepository.findByUserIdAndPostId(loggedInUser.get().getId(), postId);

        // If the like exists by user then remove it i.e. unlike
        if(existingLike.isPresent()){
            likeRepository.delete(existingLike.get());
            return false; // -- Post Was UnLike
        } else {
            // If the like doesn't exist by a user, then create it (like)
            PostLike newLike = new PostLike();
            newLike.setUser(loggedInUser.get());
            newLike.setPost(post);
            likeRepository.save(newLike);
            return true; // Post was liked
        }
    }

    @Override
    public boolean hasUserLikedThePost(Long userId, Long postId){
        return likeRepository.existsByUserIdAndPostId(userId, postId);
    }

    @Override
    public int getLikeCountForPost(Long postId){
        return likeRepository.countByPostId(postId);
    }

    @Override
    public List<PostLike> getLikesForPost(Long postId){
        return likeRepository.findByPostId(postId);
    }
}
