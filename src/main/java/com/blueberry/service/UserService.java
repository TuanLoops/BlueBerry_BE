package com.blueberry.service;

import com.blueberry.model.acc.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    User save(User User);

    Iterable<User> findAll();

    Optional<User> findByEmail(String email);

    User getCurrentEmail();

    Optional<User> findById(Long id);

    UserDetails loadAccountById(Long id);

    boolean checkLogin(User User);

    boolean isRegister(String email);

    boolean isCorrectConfirmPassword(User User);
}
