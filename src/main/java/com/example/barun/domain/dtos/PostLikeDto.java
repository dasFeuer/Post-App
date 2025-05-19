package com.example.barun.domain.dtos;

import com.example.barun.domain.PostSummaryDto;
import com.example.barun.domain.UserSummaryDto;

public class PostLikeDto {
    private Long id;
    private UserSummaryDto user;
    private PostSummaryDto post;

    public PostLikeDto() {
    }

    public PostLikeDto(Long id, UserSummaryDto user, PostSummaryDto post) {
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

    public UserSummaryDto getUser() {
        return user;
    }

    public void setUser(UserSummaryDto user) {
        this.user = user;
    }

    public PostSummaryDto getPost() {
        return post;
    }

    public void setPost(PostSummaryDto post) {
        this.post = post;
    }
}