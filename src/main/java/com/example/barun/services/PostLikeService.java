package com.example.barun.services;

import com.example.barun.domain.entities.PostLike;

import java.util.List;

public interface PostLikeService {
    boolean toggleLike(Long postId);
    boolean hasUserLikedThePost(Long userId, Long postId);
    int getLikeCountForPost(Long postId);
    List<PostLike> getLikesForPost(Long postId);
}
