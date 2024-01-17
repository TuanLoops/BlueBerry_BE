package com.blueberry.model.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AppUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String avatarImage;
    private String bannerImage;
    private LocalDateTime lastOnline;
}
