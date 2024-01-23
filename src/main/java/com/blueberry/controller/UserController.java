package com.blueberry.controller;

import com.blueberry.model.acc.JwtResponse;
import com.blueberry.model.acc.User;
import com.blueberry.model.dto.MessageResponse;
import com.blueberry.model.request.UserRequest;
import com.blueberry.service.RegisterService;
import com.blueberry.service.RoleService;
import com.blueberry.service.UserService;
import com.blueberry.service.impl.JwtService;
import com.blueberry.service.token.TokenStore;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth/api/users")
@CrossOrigin("*")
@AllArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private RegisterService registerService;
    private TokenStore tokenStore;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtService.generateTokenLogin(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> currentUser = userService.findByEmail(user.getEmail());
            if (currentUser.get().isActivated()) {
                tokenStore.storeToken(jwt);
                return ResponseEntity.ok(new JwtResponse(jwt, currentUser.get().getId(), userDetails.getUsername(), userDetails.getAuthorities()));
            }
            return new ResponseEntity<>(new MessageResponse("Account has not been activated !!"), HttpStatus.FORBIDDEN);

        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new MessageResponse("Email or password incorrect !!"), HttpStatus.FORBIDDEN);
        }
    }



    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer "))
        {
           authHeader= authHeader.replace("Bearer ", "");
        }
        tokenStore.removeToken(authHeader);
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity<>(new MessageResponse("Logout successful !!"),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
        if (userService.isRegister(userRequest.getEmail())) {
            return new ResponseEntity<>(new MessageResponse("Email has been used !!"), HttpStatus.CONFLICT);
        }
        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
            return new ResponseEntity<>( new MessageResponse("Confirm password is wrong !!"), HttpStatus.BAD_REQUEST);
        }
        return registerService.register(userRequest);
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<?> confirmUser(@RequestParam String token) {
        return registerService.verificationUser(token);
    }

    @GetMapping("/resend-email")
    private ResponseEntity<?> sendEmail(@RequestParam String email) {
        if (email == null) {
            return new ResponseEntity<>(new MessageResponse("Please enter your email !!"), HttpStatus.BAD_REQUEST);
        }
        return registerService.reSendEmail(email);
    }

    @PutMapping("/change-password")
    private ResponseEntity<?> changePassword(@RequestBody UserRequest userRequest) {
        User currentUser = userService.getCurrentUser();
        if (passwordEncoder.matches(userRequest.getOldPassword(), currentUser.getPassword())) {
            currentUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            userService.save(currentUser);
            return new ResponseEntity<>(new MessageResponse("Change password successfully !!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Old password is not correct !!"), HttpStatus.FORBIDDEN);
    }

    @GetMapping("reset-password")
    private ResponseEntity<?> resetPassword(@RequestParam String email){
        if (email == null) {
            return new ResponseEntity<>(new MessageResponse("Please enter your email !!"), HttpStatus.BAD_REQUEST);
        }
        String result = userService.sendEmail(email);
        return new ResponseEntity<>(new MessageResponse("OK"),HttpStatus.OK);
    }

}
