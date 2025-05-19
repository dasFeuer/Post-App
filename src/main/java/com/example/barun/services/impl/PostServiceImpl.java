package com.example.barun.services.impl;

import com.example.barun.domain.CreatePostRequest;
import com.example.barun.domain.PatchPostRequest;
import com.example.barun.domain.UpdatePostRequest;
import com.example.barun.domain.entities.Post;
import com.example.barun.domain.entities.User;
import com.example.barun.repositories.PostRepository;
import com.example.barun.services.PostService;
import com.example.barun.services.UserService;
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
    private UserService userService;

    @Override
    @Transactional
    public Post createPostByUser(CreatePostRequest createPostRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> author = userService.getUserByEmail(email);
        System.out.println("Email: " + email);

        if (!email.isEmpty()) {
            if (author.isPresent()) {
                Post newPost = new Post();
                newPost.setTitle(createPostRequest.getTitle());
                newPost.setContent(createPostRequest.getContent());
                newPost.setAuthor(author.get());

                return postRepository.save(newPost);
            }
        }
        throw new UsernameNotFoundException("User not found with email: " + email);
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
    public void deletePostImageById(Long postId) {
        Optional<Post> existingPostImage = getPostById(postId);
        if(existingPostImage.isPresent()){
            Post postImage = existingPostImage.get();
            postImage.setPostImageData(null);
            postImage.setPostImageType(null);
            postImage.setPostImageName(null);
            postRepository.save(postImage);
        } else {
            throw new EntityNotFoundException("Post Image not found!");
        }
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
    public Post updatePost(Long postId, UpdatePostRequest updatePostRequest) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist with id " + postId));
        existingPost.setTitle(updatePostRequest.getTitle());
        String postContent = updatePostRequest.getContent();
        existingPost.setContent(postContent);

        return postRepository.save(existingPost);

    }


//    @Override
//    public Post updatePost(Long postId, UpdatePostRequest updatePostRequest) {
//        Optional<Post> existingPost = postRepository.findById(postId);
//        if(existingPost.isPresent()){
//            Post updatedPost = existingPost.get();
//            updatedPost.setTitle(updatePostRequest.getTitle());
//            updatedPost.setContent(updatePostRequest.getContent());
//            return postRepository.save(updatedPost);
//        } else {
//            throw new EntityNotFoundException("Post not found");
//        }
//    }

    @Override
    public Post patchPost(Long postId, PatchPostRequest patchPostRequest) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if(existingPost.isPresent()){
            Post updatedPost = existingPost.get();
            if(patchPostRequest.getTitle() != null){
                updatedPost.setTitle(patchPostRequest.getTitle());
            }
            if(patchPostRequest.getContent() != null){
                updatedPost.setContent(patchPostRequest.getContent());
            }
            return postRepository.save(updatedPost);
        }
        throw new EntityNotFoundException("Post not found");
    }

    @Override
    public Post getUserPostByAuthorUsername(String username) {
        if(!username.isEmpty()) {
            return postRepository.findUserPostByAuthorUsername(username);
        } else {
            throw new EntityNotFoundException("User not found with username: " + username);
        }
    }
}
