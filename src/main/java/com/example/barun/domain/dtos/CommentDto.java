package com.example.barun.domain.dtos;

import com.example.barun.domain.PostSummaryDto;

public class CommentDto {
    private Long id;
    private String comment;
    private AuthorDto author;
    private PostSummaryDto post;

    public CommentDto() {
    }

    public CommentDto(Long id, String comment, AuthorDto author, PostSummaryDto post) {
        this.id = id;
        this.comment = comment;
        this.author = author;
        this.post = post;
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

    public PostSummaryDto getPost() {
        return post;
    }

    public void setPost(PostSummaryDto post) {
        this.post = post;
    }

}
