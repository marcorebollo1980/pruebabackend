package com.redsocial.post.seeder;

import com.redsocial.post.model.Post;
import com.redsocial.post.service.PostService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PostSeeder {

    private final PostService postService;

    public PostSeeder(PostService postService) {
        this.postService = postService;
    }

    @PostConstruct
    public void init() {
        if (postService.getAllPosts().isEmpty()) {
            Post post1 = new Post(null, "¡Hola a todos! Mi primera publicación.", 1L, LocalDateTime.now().minusHours(2), 0);
            postService.createPost(new com.redsocial.post.dto.CreatePostRequest(post1.getMessage()), post1.getUserId());

            Post post2 = new Post(null, "Disfrutando de un buen día.", 2L, LocalDateTime.now().minusHours(1), 0);
            postService.createPost(new com.redsocial.post.dto.CreatePostRequest(post2.getMessage()), post2.getUserId());

            Post post3 = new Post(null, "¡Qué interesante proyecto!", 1L, LocalDateTime.now(), 0);
            postService.createPost(new com.redsocial.post.dto.CreatePostRequest(post3.getMessage()), post3.getUserId());
        }
    }
}

