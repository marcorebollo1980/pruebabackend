package com.redsocial.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String message;
    private Long userId;
    private LocalDateTime publicationDate;
    private Integer likesCount;
}

