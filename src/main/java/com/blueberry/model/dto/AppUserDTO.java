package com.blueberry.model.dto;

import com.blueberry.model.app.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
public class AppUserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private int phoneNumber;
    private String avatarImage;
    private String bannerImage;
    private String hobbies;
    private String address;
    private String email;
}
