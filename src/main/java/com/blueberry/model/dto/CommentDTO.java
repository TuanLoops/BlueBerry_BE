package com.blueberry.model.dto;

import com.blueberry.model.app.Image;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private Long statusId;
    private AppUserDTO author;
    private String body;
    private LocalDateTime createdAt;
    private Image image;
    private boolean isUpdated;
    private int countLikes;
    private boolean liked;
}
