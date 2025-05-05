package com.example.barun.domain.dtos;

import com.example.barun.domain.entities.Post;

public class CommentDto {
    private Long id;
    private String comment;
    private AuthorDto author;
    private Post post;

    public CommentDto() {
    }

    public CommentDto(Long id, String comment, AuthorDto author, Post post) {
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
