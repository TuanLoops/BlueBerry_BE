package com.blueberry.service;

import com.blueberry.model.dto.UserRequest;
import org.springframework.http.ResponseEntity;

public interface RegisterService {
    ResponseEntity<?> register(UserRequest userRequest);
    ResponseEntity<?> verificationUser(String token);
    ResponseEntity<?> reSendEmail(String email);
}
