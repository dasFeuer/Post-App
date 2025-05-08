package com.example.barun.domain.dtos;

import com.example.barun.domain.entities.Post;
import com.example.barun.domain.entities.User;

public class PostLikeDto {
    private Long id;
    private User user;
    private Post post;

    public PostLikeDto() {
    }

    public PostLikeDto(Long id, User user, Post post) {
        this.id = id;
        this.user = user;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}