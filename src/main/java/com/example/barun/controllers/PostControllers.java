package com.example.barun.controllers;

import com.example.barun.domain.CreatePostRequest;
import com.example.barun.domain.PatchPostRequest;
import com.example.barun.domain.UpdatePostRequest;
import com.example.barun.domain.dtos.*;
import com.example.barun.domain.entities.Post;
import com.example.barun.domain.entities.User;
import com.example.barun.mappers.PostMapper;
import com.example.barun.services.PostService;
import com.example.barun.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostControllers {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostMapper postMapper;

    private boolean isPostOwnedByUser(Long postId, User user){
        Optional<Post> post = postService.getPostById(postId);
        return post.isPresent() && post.get().getAuthor().getId().equals(user.getId());
    }

    private User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.getUserByUsername(username);
    }

    private ResponseEntity<String> unauthorizedUser(){
        return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
    }


    @PostMapping("/createPost")
    public ResponseEntity<PostDto> create(@RequestBody CreatePostRequestDto createPostRequestDto){
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);
        Post createdPost = postService.createPostByUser(createPostRequest);
        PostDto createPostDto = postMapper.toDto(createdPost);
        return new ResponseEntity<>(createPostDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/post")
    public ResponseEntity<PostDto>findPostById(@PathVariable Long id){
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

        Optional<Post> posts = postService.getPostById(id);
        if (posts.isPresent()){
            PostDto postDto = postMapper.toDto(posts.get());
            return ResponseEntity.ok(postDto);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<List<PostDto>> findAllPost(){
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<Post> posts = postService.getAllPosts();
        List<PostDto> postDtos = posts.stream().map(post -> postMapper.toDto(post)).toList();
        return ResponseEntity.ok(postDtos);
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
        Optional<Post> postById = postService.getPostById(id);
        if(postById.isPresent()){
            if (isPostOwnedByUser(id, loggedInUser)){
                postService.deletePostById(id);
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
        Optional<Post> postById = postService.getPostById(postId);

        if(loggedInUser.getImageData() != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Image already present, use update image request!");
        }
        if(postById.isPresent()){
            if(isPostOwnedByUser(postId, loggedInUser)){
                Post post = postService.addImage(postId, multipartFile);
                PostImageResponseDto postImageResponseDto = new PostImageResponseDto();
                postImageResponseDto.setImageName(post.getPostImageName());
                postImageResponseDto.setImageType(post.getPostImageType());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(postImageResponseDto);
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

        Optional<Post> post = postService.getPostById(postId);
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
    public ResponseEntity<?> updateImage(
            @PathVariable Long postId,
            @RequestPart("file") MultipartFile multipartFile)
            throws IOException {
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Post> postById = postService.getPostById(postId);

        if (loggedInUser.getImageData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("First add/upload the image to update it");
        }

        if(postById.isPresent()){
            if(isPostOwnedByUser(postId, loggedInUser)){
                Post post = postService.updateImage(postId, multipartFile);
                PostImageResponseDto postImageResponseDto = new PostImageResponseDto();
                postImageResponseDto.setImageName(post.getPostImageName());
                postImageResponseDto.setImageType(post.getPostImageType());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(postImageResponseDto);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping("{postId}/updatePost")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto)
            throws Exception {
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Post> postById = postService.getPostById(postId);
        if(postById.isPresent()){
            if(isPostOwnedByUser(postId, loggedInUser)){
                UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
                Post updatedPost = postService.updatePost(postId, updatePostRequest);
                PostDto updatedPostDto = postMapper.toDto(updatedPost);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedPostDto);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
 }

    @PatchMapping("{postId}/patchPost")
    public ResponseEntity<PostDto> patchPost(
            @PathVariable Long postId,
            @RequestBody PatchPostRequestDto patchPostRequestDto)
            throws Exception {
        User loggedInUser = getAuthenticatedUser();
        if (loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Post> postById = postService.getPostById(postId);
        if (postById.isPresent()) {
            if (isPostOwnedByUser(postId, loggedInUser)) {
                PatchPostRequest patchPostRequest = postMapper.toPatchPostRequest(patchPostRequestDto);
                Post patchedPost = postService.patchPost(postId, patchPostRequest);
                PostDto patchedPostDto = postMapper.toDto(patchedPost);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(patchedPostDto);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
