package com.example.barun.controllers;

import com.example.barun.domain.dtos.CreatePostRequestDto;
import com.example.barun.domain.entities.Post;
import com.example.barun.domain.entities.User;
import com.example.barun.services.impl.PostServiceImpl;
import com.example.barun.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostControllers {

    @Autowired
    private PostServiceImpl postServiceImpl;

    @Autowired
    private UserServiceImpl userServiceImpl;

    private boolean isPostOwnedByUser(Long postId, User user){
        Optional<Post> post = postServiceImpl.getPostById(postId);
        return post.isPresent() && post.get().getAuthor().getId().equals(user.getId());
    }

    private User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userServiceImpl.getUserByUsername(username);
    }

    private ResponseEntity<String> unauthorizedUser(){
        return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/createPost")
    public ResponseEntity<Post> create(@RequestBody CreatePostRequestDto createPostRequestDto){
        Post createdPost = postServiceImpl.createPostByUser(createPostRequestDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/post")
    public ResponseEntity<?>findPostById(@PathVariable Long id){
//        User loggedInUser = getAuthenticatedUser();
//        if(loggedInUser == null){
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//
//        Optional<Post> postById = postServiceImpl.getPostById(id);
//        if(postById.isPresent()){
//            if (isPostOwnedByUser(id, loggedInUser)){
//                return ResponseEntity.ok(postById.get());
//            } else {
//                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//            }
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Optional<Post> posts = postServiceImpl.getPostById(id);
        if(posts.isEmpty()){
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(posts.get());
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<?> findAllPost(){
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null){
            return unauthorizedUser();
        }
        return ResponseEntity.ok(postServiceImpl.getAllPosts());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deletePostById(@PathVariable Long id){
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
//        List<Post> posts = loggedInUser.getPosts();
//        postServiceImpl.deletePostById(id);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(posts);
        Optional<Post> postById = postServiceImpl.getPostById(id);
        if(postById.isPresent()){
            if (isPostOwnedByUser(id, loggedInUser)){
                postServiceImpl.deletePostById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{postId}/post")
    public ResponseEntity<?> addImage(@PathVariable Long postId,
                                         @RequestPart("file")MultipartFile multipartFile)
            throws IOException {
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null){
            return unauthorizedUser();
        }
        Optional<Post> postById = postServiceImpl.getPostById(postId);
        if(postById.isPresent()){
            if(isPostOwnedByUser(postId, loggedInUser)){
                Post post = postServiceImpl.addImage(postId, multipartFile);
                return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<byte[]> getImageByPostId(@PathVariable Long postId) {
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Post> post = postServiceImpl.getPostById(postId);
        if(post.isPresent()){
            if(isPostOwnedByUser(postId, loggedInUser)){
                byte[] postImageFile = post.get().getPostImageData(); //--> Yes Optional<Post> = post.get().getImageData()
                return ResponseEntity.ok()
                        .contentType(MediaType.valueOf(post.get().getPostImageType()))
                        .body(postImageFile);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("{postId}/updatePostImage")
    public ResponseEntity<Post> updateImage(
            @PathVariable Long postId,
            @RequestPart("file") MultipartFile multipartFile)
            throws IOException {
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Post> postById = postServiceImpl.getPostById(postId);
        if(postById.isPresent()){
            if(isPostOwnedByUser(postId, loggedInUser)){
                Post post = postServiceImpl.updateImage(postId, multipartFile);
                return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping("{postId}/updatePost")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestBody CreatePostRequestDto createPostRequestDto)
            throws Exception {
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Post> postById = postServiceImpl.getPostById(postId);
        if(postById.isPresent()){
            if(isPostOwnedByUser(postId, loggedInUser)){
                Post post = postServiceImpl.updatePost(postId, createPostRequestDto);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(post);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("{postId}/patchPost")
    public ResponseEntity<Post> patchPost(
            @PathVariable Long postId,
            @RequestBody CreatePostRequestDto createPostRequestDto)
            throws Exception {
        User loggedInUser = getAuthenticatedUser();
        if (loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Post> postById = postServiceImpl.getPostById(postId);
        if (postById.isPresent()) {
            if (isPostOwnedByUser(postId, loggedInUser)) {
                Post post = postServiceImpl.patchPost(postId, createPostRequestDto);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(post);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
