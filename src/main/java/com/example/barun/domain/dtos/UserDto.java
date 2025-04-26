package com.example.barun.domain.dtos;

import com.example.barun.domain.entities.Comments;
import com.example.barun.domain.entities.Post;
import com.example.barun.domain.entities.PostLike;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Lob;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDto {
    private Long id;
    @Lob
    @JsonIgnore
    @JdbcTypeCode(Types.LONGVARBINARY)
    private byte[] imageData;
    private String imageType;
    private String imageName;
    private String fullName;
    private String username;
    private String email;
    private String password;
    private List<Post> posts = new ArrayList<>();
    private List<Comments> comments = new ArrayList<>();
    private List<PostLike> likes = new ArrayList<>();
    private Date createdAt;
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
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

