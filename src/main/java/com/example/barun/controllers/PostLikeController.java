package com.example.barun.controllers;

import com.example.barun.domain.dtos.LikeResponseDto;
import com.example.barun.domain.dtos.PostLikeDto;
import com.example.barun.domain.entities.PostLike;
import com.example.barun.domain.entities.User;
import com.example.barun.mappers.PostLikeMapper;
import com.example.barun.services.PostLikeService;
import com.example.barun.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/likes")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostLikeMapper postLikeMapper;

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeResponseDto> toggleLike(@PathVariable Long postId){
        try{
            boolean isLiked = postLikeService.toggleLike(postId);

            int likeCount = postLikeService.getLikeCountForPost(postId);

//            LikeResponseDto responseDto = new LikeResponseDto(isLiked, likeCount);
            LikeResponseDto responseDto = new LikeResponseDto();
            responseDto.setLiked(isLiked);
            responseDto.setLikeCount(likeCount);

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
            String email = authentication.getName();
            Optional<User> currentUser = userService.getUserByEmail(email);
            if (currentUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            boolean isLiked = postLikeService.hasUserLikedThePost(currentUser.get().getId(), postId);
            int likeCount = postLikeService.getLikeCountForPost(postId);

            LikeResponseDto response = new LikeResponseDto();
            response.setLiked(isLiked);
            response.setLikeCount(likeCount);


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


    @GetMapping("/{postId}/likes")
    public List<PostLikeDto> findLikesForPost(@PathVariable Long postId) {
        List<PostLike> likes = postLikeService.getLikesForPost(postId);
        return likes.stream()
                .map(postLikeMapper::toDto)
                .toList();
    }
}
