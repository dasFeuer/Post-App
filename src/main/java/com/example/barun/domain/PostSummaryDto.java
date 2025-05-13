package com.example.barun.domain;

import com.example.barun.domain.dtos.AuthorDto;

public class PostSummaryDto {

    private Long id;
    private String title;
    private String content;
    private AuthorDto author;


    public PostSummaryDto() {
    }

    public PostSummaryDto(Long id, String title, String content, AuthorDto author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }
}
