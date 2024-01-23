package com.blueberry.service.impl;


import com.blueberry.model.acc.User;
import com.blueberry.model.acc.UserPrinciple;
import com.blueberry.model.app.AppUser;
import com.blueberry.model.dto.MessageResponse;
import com.blueberry.repository.AppUserRepository;
import com.blueberry.repository.UserRepository;
import com.blueberry.service.AppUserService;
import com.blueberry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtService jwtService;
    private final Long EXPIRE_TIME = 86400000L;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        if (this.checkLogin(user.get())) {
            return UserPrinciple.build(user.get());
        }

        boolean enable = false;
        boolean accountNonExpired = false;
        boolean credentialsNonExpired = false;
        boolean accountNonLocked = false;
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(),
                user.get().getPassword(), enable, accountNonExpired, credentialsNonExpired,
                accountNonLocked, null);
    }


    @Override
    public User save(User User) {
        return userRepository.save(User);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getCurrentUser() {
        User user;
        String email;
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principle instanceof UserDetails) {
            email = ((UserDetails) principle).getUsername();
        } else {
            email = principle.toString();
        }
        user = this.findByEmail(email).get();
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }


    @Override
    public UserDetails loadUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NullPointerException();
        }
        return UserPrinciple.build(user.get());
    }

    @Override
    public boolean checkLogin(User User) {
        Iterable<User> users = this.findAll();
        boolean isCorrectUser = false;
        for (User currentUser : users) {
            if (currentUser.getEmail().equals(User.getEmail()) && currentUser.getPassword().equals(User.getPassword())&& !currentUser.isBanned()) {
                isCorrectUser = true;
                break;
            }
        }
        return isCorrectUser;
    }

    @Override
    public boolean isRegister(String email) {
       Optional<User> user = this.findByEmail(email);
        return user.isPresent();
    }

    public boolean isCorrectConfirmPassword(User User) {
        return false;
    }

    @Override
    public String sendEmail(String email) {
        Optional<User> user = this.findByEmail(email);
        if (user.isPresent()) {
            AppUser appUser = appUserRepository.findByUser(user.get()).get();
            String token = jwtService.generateEmailToken(email, EXPIRE_TIME);
            String fullName = appUser.getFirstName()+" "+appUser.getLastName();
            emailService.send(email,"Blueberry - Password Reset Request", buildMail(fullName, "http://localhost:5173/reset-password?token=" + token));
            return "Success";
        }
        return "Failure";
    }

    private String buildMail(String name, String link) {
        return "<div style=\"font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;\">" +
                "    <div style=\"background-color: #ffffff; max-width: 600px; margin: 0 auto; padding: 20px; border-radius: 5px;\">" +
                "        <h2 style=\"color: #007BFF;\">Password Reset Request</h2>" +
                "        <p>Dear <strong>"+name+"</strong>,</p>" +
                "        <p>We recently received a request to reset the password for your account. To ensure the security of your account, please follow the instructions below to set a new password:</p>" +
                "        <p>Click on the following link to access the password reset page:</p>" +
                "        <p>" +
                "            <a href="+link+" style=\"background-color: #007BFF; color: #ffffff; text-decoration: none; padding: 10px 20px; border-radius: 5px; display: inline-block;\">" +
                "                Change password" +
                "            </a>" +
                "        </p>\n" +
                "        <p>If you did not request a password reset, please disregard this email. Your account security is important to us, and we take every measure to protect it.</p>" +
                "        <p>Thank you for your prompt attention to this matter.</p>" +
                "        <p>Best regards,</p>" +
                "        <p style=\"color: #007BFF; font-weight: bold;\">Blueberry</p>" +
                "    </div>" +
                "</div>";
    }
}
