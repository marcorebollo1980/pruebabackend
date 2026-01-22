package com.redsocial.post.controller;

import com.redsocial.post.dto.CreatePostRequest;
import com.redsocial.post.dto.PostResponse;
import com.redsocial.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId;
        try {
            userId = Long.parseLong(username);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        PostResponse newPost = postService.createPost(request, userId);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId;
        try {
            userId = Long.parseLong(username);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        boolean liked = postService.likePost(id, userId);
        if (liked) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlikePost(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId;
        try {
            userId = Long.parseLong(username);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        boolean unliked = postService.unlikePost(id, userId);
        if (unliked) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}

