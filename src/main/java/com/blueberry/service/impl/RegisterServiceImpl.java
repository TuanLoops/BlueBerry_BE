package com.blueberry.service.impl;

import com.blueberry.model.acc.Role;
import com.blueberry.model.acc.User;
import com.blueberry.model.app.AppUser;
import com.blueberry.model.dto.UserRequest;
import com.blueberry.service.AppUserService;
import com.blueberry.service.RegisterService;
import com.blueberry.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private EmailService emailService;
    @Override
    public ResponseEntity<?> register(UserRequest userRequest) {
        System.out.println(userRequest.toString());
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1L,""));
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRoleList(roles);
        try {
            user= userService.save(user);
            System.out.println(user.toString());
            AppUser userApp = new AppUser();
            userApp.setFirstName(userRequest.getFirstName());
            userApp.setLastName(userRequest.getLastName());
            userApp.setUser(user);
            appUserService.save(userApp);
            String fullName = userRequest.getFirstName()+" "+userRequest.getLastName();
           emailService.send(userRequest.getEmail(),buildMail(fullName,"http://localhost:8080/api/auth/register/confirm?token=???"));
            return new ResponseEntity<>("Thành Công", HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    private String buildMail(String name, String link){
        return "<div style='font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;'>" +
                "<div style='background-color: #ffffff; max-width: 600px; margin: 0 auto; padding: 20px; border-radius: 5px;'>" +
                "<h2 style='color: #007BFF;'>Confirm your email</h2>" +
                "<p>Xin chào " + name + ",</p>" +
                "<p>Chào mừng bạn tham gia mạng xã hội <b style='color: blue'>Blueberry</b>. Vui lòng nhấn vào đường linh bên dưới để kích hoạt tài khoản:</p>" +
                "<p><a href='" + link + "' style='background-color: #007BFF; color: #ffffff; text-decoration: none; padding: 10px 20px; border-radius: 5px;'>" +
                "Kích Hoạt</a></p>" +
                "<p>Link hết hạn sau 30 phút.</p>" +
                "<p>Hẹn gặp lại</p>" +
                "</div>" +
                "</div>";
    }
}
