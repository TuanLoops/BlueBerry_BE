package com.blueberry.model.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserRequest {
    private String email;
    private String oldPassword;
    private String password;
    private String confirmPassword;
    private String lastName;
    private String firstName;
}
