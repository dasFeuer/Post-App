package com.example.barun.services;

import com.example.barun.entities.postEntities.Post;
import com.example.barun.dto.PostRequestDto;
import com.example.barun.entities.userEntities.User;
import com.example.barun.repositories.PostRepository;
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
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Post createPostByUser(PostRequestDto postRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User author = userService.getUserByUsername(username);
        System.out.println("Username: " + username);

        if (!username.isEmpty()) {
            Post newPost = new Post();
            newPost.setTitle(postRequestDto.getTitle());
            newPost.setContent(postRequestDto.getContent());
            newPost.setAuthor(author);

            return postRepository.save(newPost);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

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

    public Post updateImage(Long postId, MultipartFile multipartFile) throws IOException {
        return updateOrAddTheImage(postId, multipartFile);
    }

    public Post addImage(Long postId, MultipartFile multipartFile) throws IOException {
        return updateOrAddTheImage(postId, multipartFile);
    }


    public Post updatePost(Long postId, PostRequestDto postRequestDto) throws Exception {
        Optional<Post> existingPost = postRepository.findById(postId);
        if(existingPost.isPresent()){
            Post updatedPost = existingPost.get();
            updatedPost.setTitle(postRequestDto.getTitle());
            updatedPost.setContent(postRequestDto.getContent());
            return postRepository.save(updatedPost);
        } else {
            throw new IOException("Post not found");
        }
    }

    public Post patchPost(Long postId, PostRequestDto postRequestDto) throws Exception {
        Optional<Post> existingPost = postRepository.findById(postId);
        if(existingPost.isPresent()){
            Post updatedPost = existingPost.get();
            if(postRequestDto.getTitle() != null){
                updatedPost.setTitle(postRequestDto.getTitle());
            }
            if(postRequestDto.getContent() != null){
                updatedPost.setContent(postRequestDto.getContent());
            }
            return postRepository.save(updatedPost);
        }
        throw new IOException("Post not found");
    }
}
