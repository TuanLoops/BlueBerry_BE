package com.blueberry.controller;

import com.blueberry.model.acc.JwtResponse;
import com.blueberry.model.acc.User;
import com.blueberry.model.dto.UserRequest;
import com.blueberry.service.RoleService;
import com.blueberry.service.UserService;
import com.blueberry.service.impl.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/api/auth")
@CrossOrigin("*")
@AllArgsConstructor
public class UserController {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findByEmail(user.getEmail());
        return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getUserId(), userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest){
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
