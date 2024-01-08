package com.blueberry.controller;

import com.blueberry.model.acc.User;
import com.blueberry.model.app.AppUser;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.service.impl.AppUserServiceImpl;
import com.blueberry.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/appusers/api/auth")
@CrossOrigin("*")
public class AppUserController {
    @Autowired
    private AppUserServiceImpl appUserService;
    private ModelMapper modelMapper = new ModelMapper();
    private UserServiceImpl userService;
    @GetMapping("/{userId}")
    public ResponseEntity<AppUserDTO> getAppUser(@PathVariable Long userId) {
        Optional<AppUser> appUser = appUserService.findById(userId);
        if (appUser.isPresent()) {
            return new ResponseEntity<>(modelMapper.map(appUser,AppUserDTO.class),HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @PutMapping("/{userId}")
    public ResponseEntity<AppUserDTO> updateAppUser(@PathVariable Long userId, @RequestBody AppUserDTO appUserDTO) {
        User user = userService.getCurrentEmail();
        Optional<AppUser> appUser = appUserService.findById(user.getUserId());
        if (appUser.isPresent()) {
            AppUser appUserOld= appUser.get();
            AppUser appUserEdit =modelMapper.map(appUserDTO,AppUser.class);
            appUserEdit.setUser(appUserOld.getUser());
            appUserEdit.setUserId(appUserOld.getUserId());
            appUserEdit.setAvatarImage(appUserOld.getAvatarImage());
            appUserEdit.setBannerImage(appUserOld.getBannerImage());
            appUserService.save(appUserEdit);
            return new ResponseEntity<>(modelMapper.map(appUserEdit,AppUserDTO.class),HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
