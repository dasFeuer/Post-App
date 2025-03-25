package com.example.barun.services;

import com.example.barun.dto.CommentDto;
import com.example.barun.entities.commentEntities.Comments;
import com.example.barun.entities.postEntities.Post;
import com.example.barun.entities.userEntities.User;
import com.example.barun.repositories.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Transactional
    public Comments writeCommentOnUserPost(Long postId, CommentDto commentDto) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User existingUser = userService.getUserByUsername(username);
        Optional<Post> existingPost = postService.getPostById(postId);

        if(existingPost.isPresent()) {
            Comments newComment = new Comments();
            newComment.setComment(commentDto.getComment());
            newComment.setAuthor(existingUser);
            return commentRepository.save(newComment);
        }
        throw new IOException("Post not found!");
    }
}
