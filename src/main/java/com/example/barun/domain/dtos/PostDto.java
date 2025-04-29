package com.example.barun.domain.dtos;

import com.example.barun.domain.entities.Comments;
import com.example.barun.domain.entities.PostLike;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDto {

    private Long id;
    private String title;
    private String content;
    private AuthorDto author;
    private byte[] postImageData;
    private String postImageType;
    private String postImageName;
    private List<Comments> comments = new ArrayList<>();
    private List<PostLike> likes = new ArrayList<>();
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
                   List<Comments> comments,
                   List<PostLike> likes,
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
        this.likes = likes;
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

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public List<PostLike> getLikes() {
        return likes;
    }

    public void setLikes(List<PostLike> likes) {
        this.likes = likes;
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