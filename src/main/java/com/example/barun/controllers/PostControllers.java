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

    @PostMapping("/createPost")
    public ResponseEntity<Post> create(@RequestBody PostRequestDto postRequestDto){
        Post createdPost = postService.createPostByUser(postRequestDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/post")
    public ResponseEntity<?>findPostById(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loggedInUser = userService.getUserByUsername(username);
        List<Post> post = loggedInUser.getPosts();
        if(!post.isEmpty()){
            Optional<Post> postById = postService.getPostById(id);
            if(postById.isPresent()){
                return ResponseEntity.ok(postById.get());
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loggedInUser = userService.getUserByUsername(username);
        List<Post> posts = loggedInUser.getPosts();
        postService.deletePostById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(posts);
    }

    @PostMapping("/{postId}/post")
    public ResponseEntity<Post> addImage(@PathVariable Long postId,
                                         @RequestPart("file")MultipartFile multipartFile)
            throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loggedInUser = userService.getUserByUsername(username);
        List<Post> post = loggedInUser.getPosts();
        if(!post.isEmpty()){
            Post addedImagePost = postService.addImage(postId, multipartFile);
            return new ResponseEntity<>(addedImagePost, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<byte[]> getImageByPostId(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loggedInUser = userService.getUserByUsername(username);
        List<Post> postOfLoginUser = loggedInUser.getPosts();
        if (!postOfLoginUser.isEmpty()) {
            Optional<Post> post = postService.getPostById(postId);
            if (post.isPresent()) {
                byte[] postImageFile = post.get().getPostImageData(); // --> Yes Optional<Post> = post.get().getImageData()
                return ResponseEntity.ok()
                        .contentType(MediaType.valueOf(post.get().getPostImageType()))
                        .body(postImageFile);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("{postId}/updatePostImage")
    public ResponseEntity<Post> updateImage(
            @PathVariable Long postId,
            @RequestPart("file") MultipartFile multipartFile)
            throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loggedInUser = userService.getUserByUsername(username);
        List<Post> postImageOfLoginUser = loggedInUser.getPosts();

        if(!postImageOfLoginUser.isEmpty()){
            Post post = postService.updateImage(postId, multipartFile);
            return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("{postId}/updatePost")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto postRequestDto)
            throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loggedInUser = userService.getUserByUsername(username);
        List<Post> postOfLoginUser = loggedInUser.getPosts();

        if(!postOfLoginUser.isEmpty()){
            Post post = postService.updatePost(postId, postRequestDto);
            return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("{postId}/patchPost")
    public ResponseEntity<Post> patchPost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto postRequestDto)
            throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loggedInUser = userService.getUserByUsername(username);
        List<Post> postOfLoginUser = loggedInUser.getPosts();

        if(!postOfLoginUser.isEmpty()){
            Post post = postService.patchPost(postId, postRequestDto);
            return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
