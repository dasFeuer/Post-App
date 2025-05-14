package com.example.barun.domain;

import com.example.barun.domain.dtos.AuthorDto;

public class CommentSummaryDto {
    private Long id;
    private String comment;
    private AuthorDto author;

    public CommentSummaryDto() {
    }

    public CommentSummaryDto(Long id, String comment, AuthorDto author, PostSummaryDto post) {
        this.id = id;
        this.comment = comment;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }

}
