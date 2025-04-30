package com.example.barun.services;

import com.example.barun.domain.dtos.CreatePostRequestDto;
import com.example.barun.domain.entities.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PostService {

    Post createPostByUser(CreatePostRequestDto createPostRequestDto);
    Optional<Post> getPostById(Long id);
    void deletePostById(Long id);
    Post updateOrAddTheImage(Long postId, MultipartFile multipartFile) throws IOException;
    Post updateImage(Long postId, MultipartFile multipartFile) throws IOException;
    Post addImage(Long postId, MultipartFile multipartFile) throws IOException;
    Post updatePost(Long postId, CreatePostRequestDto createPostRequestDto);
    Post patchPost(Long postId, CreatePostRequestDto createPostRequestDto);
    List<Post> getAllPosts();

}
