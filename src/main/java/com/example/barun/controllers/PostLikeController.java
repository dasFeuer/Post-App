package com.example.barun.controllers;

import com.example.barun.dto.LikeResponseDto;
import com.example.barun.entities.userEntities.User;
import com.example.barun.services.LikeService;
import com.example.barun.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/likes")
public class PostLikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeResponseDto> toggleLike(@PathVariable Long postId){
        try{
            boolean isLiked = likeService.toggleLike(postId);

            int likeCount = likeService.getLikeCountForPost(postId);

            LikeResponseDto responseDto = new LikeResponseDto(isLiked, likeCount);

            return ResponseEntity.ok(responseDto);
        } catch (IOException e) {
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
            User currentUser = userService.getUserByUsername(username);

            boolean isLiked = likeService.hasUserLikedThePost(currentUser.getId(), postId);
            int likeCount = likeService.getLikeCountForPost(postId);

            LikeResponseDto response = new LikeResponseDto(isLiked, likeCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{postId}/likesCount")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long postId) {
        int likeCount = likeService.getLikeCountForPost(postId);
        return ResponseEntity.ok(likeCount);
    }
}
