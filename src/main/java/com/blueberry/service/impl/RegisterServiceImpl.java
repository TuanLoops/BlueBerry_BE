package com.blueberry.service.impl;

import com.blueberry.model.acc.Role;
import com.blueberry.model.acc.User;
import com.blueberry.model.app.AppUser;
import com.blueberry.model.dto.MessageResponse;
import com.blueberry.model.dto.UserRequest;
import com.blueberry.service.AppUserService;
import com.blueberry.service.RegisterService;
import com.blueberry.service.UserService;
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

    private JwtService jwtService;
    private final Long EXPIRE_TIME = 86400000L;

    @Override
    public ResponseEntity<?> register(UserRequest userRequest) {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1L, ""));
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRoleList(roles);
        AppUser userApp = new AppUser();
        userApp.setFirstName(userRequest.getFirstName());
        userApp.setLastName(userRequest.getLastName());
        String fullName = userRequest.getFirstName() + " " + userRequest.getLastName();
        try {
            String token = jwtService.generateEmailToken(user.getEmail(), EXPIRE_TIME);
            user = userService.save(user);
            userApp.setUser(user);
            appUserService.save(userApp);
            emailService.send(userRequest.getEmail(), buildMail(fullName, "http://localhost:5173/confirm?token=" + token));
            return new ResponseEntity<>(new MessageResponse("Registered successfully"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
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
        if (token == null) {
            return new ResponseEntity<>( new MessageResponse("Token required"),HttpStatus.BAD_REQUEST);
        }
        if (jwtService.validateEmailToken(token)) {
            String email = jwtService.getEmailFromJwtToken(token);
            Optional<User> user = userService.findByEmail(email);
            if (user.isPresent()) {
                if(user.get().isActivated()){
                    return new ResponseEntity<>(new MessageResponse("Account has been activated"),HttpStatus.CONFLICT);
                }
                user.get().setActivated(true);
                userService.save(user.get());
                return new ResponseEntity<>(new MessageResponse("Account has been activated successfully"),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new MessageResponse("Account activation failed"),HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> reSendEmail(String email) {
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
            Optional<AppUser> appUser = appUserService.findById(user.get().getUserId());
            String token = jwtService.generateEmailToken(email, EXPIRE_TIME);
            String fullName = appUser.get().getFirstName()+" "+appUser.get().getLastName();
            emailService.send(email, buildMail(fullName, "http://localhost:8080/users/api/auth/register/confirm?token=" + token));
            return new ResponseEntity<>(new MessageResponse("Email has been sent"),HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("The email doesn't exist"),HttpStatus.BAD_REQUEST);
    }

}
