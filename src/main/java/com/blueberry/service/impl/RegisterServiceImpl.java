package com.blueberry.service.impl;

import com.blueberry.model.acc.Role;
import com.blueberry.model.acc.User;
import com.blueberry.model.app.AppUser;
import com.blueberry.model.dto.MessageResponse;
import com.blueberry.model.request.UserRequest;
import com.blueberry.service.AppUserService;
import com.blueberry.service.RegisterService;
import com.blueberry.service.RoleService;
import com.blueberry.service.UserService;
import com.blueberry.model.acc.Token;
import com.blueberry.service.token.TokenStore;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private AppUserService appUserService;
    private EmailService emailService;
    private RoleService roleService;
    private TokenStore tokenStore;

    private JwtService jwtService;
    private final Long EXPIRE_TIME = 86400000L;

    @Override
    public ResponseEntity<?> register(UserRequest userRequest) {
        List<Role> roles = new ArrayList<>();
        Role role = roleService.findByName("ROLE_USER");
        roles.add(role);
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRoleList(roles);
        AppUser userApp = new AppUser();
        userApp.setFirstName(userRequest.getFirstName());
        userApp.setLastName(userRequest.getLastName());
        String fullName = userRequest.getFirstName() + " " + userRequest.getLastName();
        try {
            String jwt = jwtService.generateEmailToken(user.getEmail(), EXPIRE_TIME);
            user = userService.save(user);
            userApp.setUser(user);
            userApp.setAvatarImage("https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909_1280.png");
            appUserService.save(userApp);
            tokenStore.storeToken(new Token(jwt,user.getEmail(),false));
            emailService.send(userRequest.getEmail(),"MXH Blueberry Xác nhận email", buildMail(fullName, "http://localhost:5173/confirm?token=" + jwt));
            return new ResponseEntity<>(new MessageResponse("Registered successfully !!"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()+" !!"), HttpStatus.BAD_REQUEST);
        }
    }

    private String buildMail(String name, String link) {
        return "<div style='font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;'>" +
                "<div style='background-color: #ffffff; max-width: 600px; margin: 0 auto; padding: 20px; border-radius: 5px;'>" +
                "<h2 style='color: #007BFF;'>Xác Nhận Email Của Bạn</h2>" +
                "<p>Xin chào " + name + ",</p>" +
                "<p>Chào mừng bạn tham gia mạng xã hội <b style='color: blue'>Blueberry</b> của chúng tôi. Vui lòng nhấn vào nút bên dưới để kích hoạt tài khoản:</p>" +
                "<p><a href='" + link + "' style='background-color: #007BFF; color: #ffffff; text-decoration: none; padding: 10px 20px; border-radius: 5px;'>" +
                "Kích Hoạt</a></p>" +
                "<p>Linh hết hạn sau 1 ngày.</p>" +
                "<p>Hẹn gặp lại</p>" +
                "</div>" +
                "</div>";
    }

    @Override
    public ResponseEntity<?> verificationUser(String token) {
        if (token == null ||token.isEmpty()) {
            return new ResponseEntity<>( new MessageResponse("Token required"),HttpStatus.BAD_REQUEST);
        }
        if (jwtService.validateEmailToken(token) && tokenStore.isTokenActive(token)) {
            String email = jwtService.getEmailFromJwtToken(token);
            Optional<User> user = userService.findByEmail(email);
            if (user.isPresent()) {
                if(user.get().isActivated()){
                    return new ResponseEntity<>(new MessageResponse("Account has been activated !!"),HttpStatus.CONFLICT);
                }
                user.get().setActivated(true);
                userService.save(user.get());
                tokenStore.removeToken(token);
                return new ResponseEntity<>(new MessageResponse("Account has been activated successfully !!"),HttpStatus.OK);
            }
        }
        tokenStore.removeToken(token);
        return new ResponseEntity<>(new MessageResponse("Account activation failed !!"),HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> reSendEmail(String email) {
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
            AppUser appUser = appUserService.findByUserName(user.get().getEmail());
            String jwt = jwtService.generateEmailToken(email, EXPIRE_TIME);
            Token  token = tokenStore.getTokenByEmail(email,false);
            if(token!=null) {
                token.setToken(jwt);
            }else {
                tokenStore.storeToken(new Token(jwt, email, false));
            }
            String fullName = appUser.getFirstName()+" "+appUser.getLastName();
            emailService.send(email,"MXH Blueberry Xác nhận email", buildMail(fullName, "http://localhost:5173/confirm?token=" + token));
            return new ResponseEntity<>(new MessageResponse("Email has been sent !!"),HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("The email doesn't exist !!"),HttpStatus.BAD_REQUEST);
    }

}
