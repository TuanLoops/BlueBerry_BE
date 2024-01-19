package com.blueberry.service;

import com.blueberry.model.acc.User;
import com.blueberry.model.acc.UserPrinciple;
import com.blueberry.model.app.AppUser;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppUserService extends GenericService<AppUser>{
    Optional<AppUser> findByUser(User user);

    AppUser findByUserName(String username);
    AppUser getCurrentAppUser();

    Iterable<AppUser> findByName(String name, boolean banned);
}
