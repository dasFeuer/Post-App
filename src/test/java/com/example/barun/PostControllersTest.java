package com.example.barun;

import com.example.barun.controllers.PostControllers;
import com.example.barun.dto.PostRequestDto;
import com.example.barun.entities.postEntities.Post;
import com.example.barun.services.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostControllersTest {

    @InjectMocks
    private PostControllers postControllers;

    @Mock
    private PostService postService;

    @Test
    public void createPost(){
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("Java Testing");
        postRequestDto.setContent("It worked");

        Post mockPost = new Post();
        mockPost.setId(1L);
        mockPost.setTitle("Java Testing");
        mockPost.setContent("It worked");
        System.out.println("Test passed");
        when(postService.createPostByUser(any(PostRequestDto.class))).thenReturn(mockPost);

        ResponseEntity<Post> responseEntity = postControllers.create(postRequestDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(mockPost, responseEntity.getBody());

        System.out.println("Test Passed");

    }
}
