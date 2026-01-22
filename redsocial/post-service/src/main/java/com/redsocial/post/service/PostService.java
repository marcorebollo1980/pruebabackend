package com.redsocial.post.service;

import com.redsocial.post.dto.CreatePostRequest;
import com.redsocial.post.dto.PostResponse;
import com.redsocial.post.model.Like;
import com.redsocial.post.model.Post;
import com.redsocial.post.repository.LikeRepository;
import com.redsocial.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public PostService(PostRepository postRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }

    @Transactional
    public PostResponse createPost(CreatePostRequest request, Long userId) {
        Post post = new Post();
        post.setMessage(request.getMessage());
        post.setUserId(userId);
        post.setPublicationDate(LocalDateTime.now());
        post.setLikesCount(0);
        Post savedPost = postRepository.save(post);
        return mapToPostResponse(savedPost);
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAllByOrderByPublicationDateDesc().stream()
                .map(this::mapToPostResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean likePost(Long postId, Long userId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (!likeRepository.existsByPostIdAndUserId(postId, userId)) {
                Like like = new Like();
                like.setPostId(postId);
                like.setUserId(userId);
                likeRepository.save(like);
                post.setLikesCount(post.getLikesCount() + 1);
                postRepository.save(post);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean unlikePost(Long postId, Long userId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            Optional<Like> likeOptional = likeRepository.findByPostIdAndUserId(postId, userId);
            if (likeOptional.isPresent()) {
                likeRepository.delete(likeOptional.get());
                post.setLikesCount(post.getLikesCount() - 1);
                postRepository.save(post);
                return true;
            }
        }
        return false;
    }

    private PostResponse mapToPostResponse(Post post) {
        PostResponse dto = new PostResponse();
        dto.setId(post.getId());
        dto.setMessage(post.getMessage());
        dto.setUserId(post.getUserId());
        dto.setPublicationDate(post.getPublicationDate());
        dto.setLikesCount(post.getLikesCount());
        return dto;
    }
}

