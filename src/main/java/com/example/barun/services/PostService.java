package com.example.barun.services;

import com.example.barun.domain.CreatePostRequest;
import com.example.barun.domain.PatchPostRequest;
import com.example.barun.domain.UpdatePostRequest;
import com.example.barun.domain.dtos.CreatePostRequestDto;
import com.example.barun.domain.entities.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PostService {

    Post createPostByUser(CreatePostRequest createPostRequest);
    Optional<Post> getPostById(Long id);
    void deletePostById(Long id);
    Post updateOrAddTheImage(Long postId, MultipartFile multipartFile) throws IOException;
    Post updateImage(Long postId, MultipartFile multipartFile) throws IOException;
    Post addImage(Long postId, MultipartFile multipartFile) throws IOException;
    Post updatePost(Long postId, UpdatePostRequest updatePostRequest);
    Post patchPost(Long postId, PatchPostRequest patchPostRequest);
    List<Post> getAllPosts();

}
