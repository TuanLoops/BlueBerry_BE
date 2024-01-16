package com.blueberry.model.dto;

import com.blueberry.model.app.Image;
import com.blueberry.model.app.PrivacyLevel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StatusDTO {
    private Long id;
    private String body;
    private LocalDateTime createdAt;
    private boolean isUpdated;
    private AppUserDTO author;
    private List<Image> imageList;
    private int countComments;
    private int countLikes;
    private PrivacyLevel privacyLevel;
    private boolean liked;
}
