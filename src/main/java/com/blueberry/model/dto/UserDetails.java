package com.blueberry.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDetails {
        private Long id;
        private String firstName;
        private String lastName;
        private String fullName;
        private LocalDate dob;
        private int phoneNumber;
        private String avatarImage;
        private String bannerImage;
        private String hobbies;
        private String address;
        private String email;
}
