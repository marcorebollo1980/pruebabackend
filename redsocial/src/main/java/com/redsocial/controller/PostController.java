package com.redsocial.controller;

import com.redsocial.dto.CreatePostRequest;
import com.redsocial.dto.PostResponse;
import com.redsocial.security.CustomUserDetails;
import com.redsocial.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Controlador de Posts - Ahora optimizado sin queries adicionales.
 * El userId se obtiene directamente del JWT a través de CustomUserDetails.
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Obtiene el userId del usuario autenticado sin hacer queries a la BD.
     * El userId viene del JWT que fue validado en el JwtRequestFilter.
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }
        throw new IllegalStateException("User not authenticated or invalid authentication object");
    }

    /**
     * Crea un nuevo post.
     * ✅ OPTIMIZADO: Sin queries adicionales - userId viene del JWT.
     */
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        Long userId = getCurrentUserId();
        PostResponse newPost = postService.createPost(request, userId);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * Da like a un post.
     * ✅ OPTIMIZADO: Sin queries adicionales - userId viene del JWT.
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Long userId = getCurrentUserId();
        boolean liked = postService.likePost(id, userId);
        if (liked) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Quita el like de un post.
     * ✅ OPTIMIZADO: Sin queries adicionales - userId viene del JWT.
     */
    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlikePost(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Long userId = getCurrentUserId();
        boolean unliked = postService.unlikePost(id, userId);
        if (unliked) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}

