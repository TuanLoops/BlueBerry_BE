package com.blueberry.service.impl;


import com.blueberry.model.acc.User;
import com.blueberry.model.acc.UserPrinciple;
import com.blueberry.repository.UserRepository;
import com.blueberry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public User getCurrentEmail() {
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
    public UserDetails loadAccountById(Long id) {
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


}
