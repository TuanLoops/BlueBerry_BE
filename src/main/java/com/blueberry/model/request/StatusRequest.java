package com.blueberry.model.request;

import com.blueberry.model.app.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class StatusRequest {
    private String body;
    private List<Image> imageList;
    private PrivacyLevel privacyLevel;
}
