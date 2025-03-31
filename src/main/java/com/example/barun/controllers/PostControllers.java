package com.example.barun.controllers;

import com.example.barun.entities.postEntities.Post;
import com.example.barun.dto.PostRequestDto;
import com.example.barun.entities.userEntities.User;
import com.example.barun.services.PostService;
import com.example.barun.services.UserService;
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
    public ResponseEntity<Post> create(@RequestBody PostRequestDto postRequestDto){
        Post createdPost = postService.createPostByUser(postRequestDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/post")
    public ResponseEntity<?>findPostById(@PathVariable Long id){
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
//        List<Post> post = loggedInUser.getPosts();
//        if(!post.isEmpty()){
//            Optional<Post> postById = postService.getPostById(id);
//            if(postById.isPresent()){
//                return ResponseEntity.ok(postById.get());
//            }
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Optional<Post> postById = postService.getPostById(id);
        if(postById.isPresent()){
            if (isPostOwnedByUser(id, loggedInUser)){
                return ResponseEntity.ok(postById.get());
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getAllPosts")
    public List<Post> findAllPost(){
        return postService.getAllPosts();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deletePostById(@PathVariable Long id){
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
//        List<Post> posts = loggedInUser.getPosts();
//        postService.deletePostById(id);
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
//        List<Post> post = loggedInUser.getPosts();
//        if(!post.isEmpty()){
//            Post addedImagePost = postService.addImage(postId, multipartFile);
//            return new ResponseEntity<>(addedImagePost, HttpStatus.ACCEPTED);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional<Post> postById = postService.getPostById(postId);
        if(postById.isPresent()){
            if(isPostOwnedByUser(postId, loggedInUser)){
                Post post = postService.addImage(postId, multipartFile);
                return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Todo --> Modify the code with helper function for authentication

    @GetMapping("/{postId}/image")
    public ResponseEntity<byte[]> getImageByPostId(@PathVariable Long postId) {
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        List<Post> postOfLoginUser = loggedInUser.getPosts();
//        if (!postOfLoginUser.isEmpty()) {
//            Optional<Post> post = postService.getPostById(postId);
//            if (post.isPresent()) {
//                byte[] postImageFile = post.get().getPostImageData(); // --> Yes Optional<Post> = post.get().getImageData()
//                return ResponseEntity.ok()
//                        .contentType(MediaType.valueOf(post.get().getPostImageType()))
//                        .body(postImageFile);
//            }
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

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
    public ResponseEntity<Post> updateImage(
            @PathVariable Long postId,
            @RequestPart("file") MultipartFile multipartFile)
            throws IOException {
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
//        List<Post> postImageOfLoginUser = loggedInUser.getPosts();
//
//        if(!postImageOfLoginUser.isEmpty()){
//            Post post = postService.updateImage(postId, multipartFile);
//            return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional<Post> postById = postService.getPostById(postId);
        if(postById.isPresent()){
            if(isPostOwnedByUser(postId, loggedInUser)){
                Post post = postService.updateImage(postId, multipartFile);
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
            @RequestBody PostRequestDto postRequestDto)
            throws Exception {
        User loggedInUser = getAuthenticatedUser();
        if(loggedInUser == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
//        List<Post> postOfLoginUser = loggedInUser.getPosts();
//
//        if(!postOfLoginUser.isEmpty()){
//            Post post = postService.updatePost(postId, postRequestDto);
//            return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Post> postById = postService.getPostById(postId);
        if(postById.isPresent()){
            if(isPostOwnedByUser(postId, loggedInUser)){
                Post post = postService.updatePost(postId, postRequestDto);
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
            @RequestBody PostRequestDto postRequestDto)
            throws Exception {
        User loggedInUser = getAuthenticatedUser();
        if (loggedInUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
//        List<Post> postOfLoginUser = loggedInUser.getPosts();
//
//        if(!postOfLoginUser.isEmpty()){
//            Post post = postService.patchPost(postId, postRequestDto);
//            return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
        Optional<Post> postById = postService.getPostById(postId);
        if (postById.isPresent()) {
            if (isPostOwnedByUser(postId, loggedInUser)) {
                Post post = postService.patchPost(postId, postRequestDto);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(post);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
