package com.example.barun.controllers;

import com.example.barun.domain.dtos.LikeResponseDto;
import com.example.barun.domain.entities.User;
import com.example.barun.services.PostLikeService;
import com.example.barun.services.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeResponseDto> toggleLike(@PathVariable Long postId){
        try{
            boolean isLiked = postLikeService.toggleLike(postId);

            int likeCount = postLikeService.getLikeCountForPost(postId);

            LikeResponseDto responseDto = new LikeResponseDto(isLiked, likeCount);

            return ResponseEntity.ok(responseDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/{postId}/likeStatus")
    public ResponseEntity<LikeResponseDto> getLikeStatus(@PathVariable Long postId) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User currentUser = userServiceImpl.getUserByUsername(username);

            boolean isLiked = postLikeService.hasUserLikedThePost(currentUser.getId(), postId);
            int likeCount = postLikeService.getLikeCountForPost(postId);

            LikeResponseDto response = new LikeResponseDto(isLiked, likeCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{postId}/likesCount")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long postId) {
        int likeCount = postLikeService.getLikeCountForPost(postId);
        return ResponseEntity.ok(likeCount);
    }
}
