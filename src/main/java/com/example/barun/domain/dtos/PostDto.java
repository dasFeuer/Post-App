package com.example.barun.domain.dtos;

import com.example.barun.domain.CommentSummaryDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDto {

    private Long id;
    private String title;
    private String content;
    private AuthorDto author;
    @JsonIgnore
    private byte[] postImageData;
    private String postImageType;
    private String postImageName;
    private List<CommentSummaryDto> comments = new ArrayList<>();
    private List<PostLikeSummaryDto> likes = new ArrayList<>();
    private Date createdAt;
    private Date updatedAt;

    public PostDto() {
    }


    public PostDto(Long id,
                   String title,
                   String content, AuthorDto author,
                   byte[] postImageData,
                   String postImageType,
                   String postImageName,
                   List<CommentSummaryDto> comments,
                   List<PostLikeSummaryDto> likes,
                   Date createdAt,
                   Date updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.postImageData = postImageData;
        this.postImageType = postImageType;
        this.postImageName = postImageName;
        this.comments = comments;

        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public byte[] getPostImageData() {
        return postImageData;
    }

    public void setPostImageData(byte[] postImageData) {
        this.postImageData = postImageData;
    }

    public String getPostImageType() {
        return postImageType;
    }

    public void setPostImageType(String postImageType) {
        this.postImageType = postImageType;
    }

    public String getPostImageName() {
        return postImageName;
    }

    public void setPostImageName(String postImageName) {
        this.postImageName = postImageName;
    }

    public List<PostLikeSummaryDto> getLikes() {
        return likes;
    }

    public void setLikes(List<PostLikeSummaryDto> likes) {
        this.likes = likes;
    }

    public List<CommentSummaryDto> getComments() {
        return comments;
    }


    public void setComments(List<CommentSummaryDto> comments) {
        this.comments = comments;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}