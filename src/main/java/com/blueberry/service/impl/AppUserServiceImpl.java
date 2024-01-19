package com.blueberry.service.impl;


import com.blueberry.model.acc.User;
import com.blueberry.model.app.AppUser;
import com.blueberry.repository.AppUserRepository;
import com.blueberry.service.AppUserService;
import com.blueberry.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private AppUserRepository appUserRepository;
    private UserService userService;
    @Override
    public Iterable<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    @Override
    public Optional<AppUser> findById(Long id) {
        return appUserRepository.findById(id);
    }

    @Override
    public AppUser save(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    @Override
    public void delete(Long id) {
        appUserRepository.deleteById(id);
    }

    @Override
    public Optional<AppUser> findByUser(User user) {
        return appUserRepository.findByUser(user);
    }

    @Override
    public AppUser findByUserName(String username) {
        Optional<User> user = userService.findByEmail(username);
        return appUserRepository.findByUser(user.get()).get();
    }

    @Override
    public AppUser getCurrentAppUser() {
        User user = userService.getCurrentUser();
        return this.findByUserName(user.getEmail());
    }

    @Override
    public Iterable<AppUser> findByName(String name, boolean banned) {
        return appUserRepository.findByName(name, banned);
    }
}
