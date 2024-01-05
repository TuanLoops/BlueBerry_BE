package com.blueberry.service;

import com.blueberry.model.acc.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    void save(User User);

    Iterable<User> findAll();

    User findByEmail(String email);

    User getCurrentEmail();

    Optional<User> findById(Long id);

    UserDetails loadAccountById(Long id);

    boolean checkLogin(User User);

    boolean isRegister(User User);

    boolean isCorrectConfirmPassword(User User);
}
