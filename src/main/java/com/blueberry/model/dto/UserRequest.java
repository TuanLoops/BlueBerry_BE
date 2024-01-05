package com.blueberry.model.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserRequest {
    private String email;
    private String password;
    private String confirmPassword;
    private String lastName;
    private String firstName;
}
