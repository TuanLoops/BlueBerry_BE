package com.blueberry.controller;

import com.blueberry.model.acc.JwtResponse;
import com.blueberry.model.acc.User;
import com.blueberry.model.dto.UserRequest;

import com.blueberry.service.RegisterService;
import com.blueberry.service.RoleService;
import com.blueberry.service.UserService;
import com.blueberry.service.impl.JwtService;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> currentUser = userService.findByEmail(user.getEmail());
        if(currentUser.get().isActivated()){
            return ResponseEntity.ok(new JwtResponse(jwt, currentUser.get().getId(), userDetails.getUsername(), userDetails.getAuthorities()));
        }
        return new ResponseEntity<>("Chưa kích hoạt",HttpStatus.FORBIDDEN);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity<>("Đăng xuất thành công !!",HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest){
        if(userService.isRegister(userRequest.getEmail())){
            return new ResponseEntity<>("Email đã được sử dụng", HttpStatus.CONFLICT);
        }
        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())){
            return new ResponseEntity<>("Xác nhận mật khẩu không khớp", HttpStatus.BAD_REQUEST);
        }
        return registerService.register(userRequest);
    }
    @GetMapping("/register/confirm")
    public ResponseEntity<?> confirmUser(@RequestParam String token){
        System.out.println(token);
        return registerService.verificationUser(token);
    }


}
