package com.blueberry.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private Long statusId;
    private AppUserDTO author;
    private String body;
    private LocalDateTime createdAt;
    private boolean isUpdated;
}
