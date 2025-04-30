package com.example.barun.services.impl;

import com.example.barun.domain.dtos.CreatePostRequestDto;
import com.example.barun.domain.entities.Post;
import com.example.barun.domain.entities.User;
import com.example.barun.repositories.PostRepository;
import com.example.barun.services.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Override
    @Transactional
    public Post createPostByUser(CreatePostRequestDto createPostRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User author = userServiceImpl.getUserByUsername(username);
        System.out.println("Username: " + username);

        if (!username.isEmpty()) {
            Post newPost = new Post();
            newPost.setTitle(createPostRequestDto.getTitle());
            newPost.setContent(createPostRequestDto.getContent());
            newPost.setAuthor(author);

            return postRepository.save(newPost);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    @Override
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post updateOrAddTheImage(Long postId, MultipartFile multipartFile) throws IOException {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            Post updatePost = post.get();
            updatePost.setPostImageData(multipartFile.getBytes());
            updatePost.setPostImageType(multipartFile.getContentType());
            updatePost.setPostImageName(multipartFile.getOriginalFilename());
            return postRepository.save(updatePost);
        } else {
            throw new IOException("Post not found!");
        }
    }

    @Override
    public Post updateImage(Long postId, MultipartFile multipartFile) throws IOException {
        return updateOrAddTheImage(postId, multipartFile);
    }

    @Override
    public Post addImage(Long postId, MultipartFile multipartFile) throws IOException {
        return updateOrAddTheImage(postId, multipartFile);
    }


    @Override
    public Post updatePost(Long postId, CreatePostRequestDto createPostRequestDto) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if(existingPost.isPresent()){
            Post updatedPost = existingPost.get();
            updatedPost.setTitle(createPostRequestDto.getTitle());
            updatedPost.setContent(createPostRequestDto.getContent());
            return postRepository.save(updatedPost);
        } else {
            throw new EntityNotFoundException("Post not found");
        }
    }

    @Override
    public Post patchPost(Long postId, CreatePostRequestDto createPostRequestDto) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if(existingPost.isPresent()){
            Post updatedPost = existingPost.get();
            if(createPostRequestDto.getTitle() != null){
                updatedPost.setTitle(createPostRequestDto.getTitle());
            }
            if(createPostRequestDto.getContent() != null){
                updatedPost.setContent(createPostRequestDto.getContent());
            }
            return postRepository.save(updatedPost);
        }
        throw new EntityNotFoundException("Post not found");
    }
}
