package com.blueberry.model.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AppUserRequest {
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private int phoneNumber;
    private String hobbies;
    private String address;
}
