package com.example.barun.domain.dtos;

import com.example.barun.domain.PostSummaryDto;
import com.example.barun.domain.UserSummaryDto;

public class PostLikeSummaryDto {
    private Long id;
    private UserSummaryDto user;

    public PostLikeSummaryDto() {
    }

    public PostLikeSummaryDto(Long id, UserSummaryDto user) {
        this.id = id;
        this.user = user;
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
}