package com.blueberry.controller;

import com.blueberry.model.acc.User;
import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Image;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.service.AppUserService;
import com.blueberry.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth/api/appusers")
@CrossOrigin("*")
@AllArgsConstructor
public class AppUserController {
    private AppUserService appUserService;
    private ModelMapper modelMapper;
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> getAppUser(@PathVariable Long id) {
        Optional<AppUser> appUser = appUserService.findById(id);
        if (appUser.isPresent()) {
            return new ResponseEntity<>(modelMapper.map(appUser,AppUserDTO.class),HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @PutMapping("/{id}")
    public ResponseEntity<AppUserDTO> updateAppUser(@PathVariable Long id, @RequestBody AppUserDTO appUserDTO) {
        User user = userService.getCurrentUser();
        Optional<AppUser> appUser = appUserService.findById(user.getId());
        if (appUser.isPresent()) {
            AppUser appUserOld= appUser.get();
            AppUser appUserEdit =modelMapper.map(appUserDTO,AppUser.class);
            appUserEdit.setUser(appUserOld.getUser());
            appUserEdit.setId(appUserOld.getId());
            appUserEdit.setAvatarImage(appUserOld.getAvatarImage());
            appUserEdit.setBannerImage(appUserOld.getBannerImage());
            appUserService.save(appUserEdit);
            return new ResponseEntity<>(modelMapper.map(appUserEdit,AppUserDTO.class),HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/change-avatar")
    public ResponseEntity<Image> changeAvatar(@RequestBody Image image){
        User user = userService.getCurrentUser();
        AppUser appUser = appUserService.findByUserName(user.getEmail());
        appUser.setAvatarImage(image.getImageLink());
        appUserService.save(appUser);
        return new ResponseEntity<>(image,HttpStatus.OK);
    }
    @PatchMapping("/change-banner")
    public ResponseEntity<Image> changeBanner(@RequestBody Image image){
        User user = userService.getCurrentUser();
        AppUser appUser = appUserService.findByUserName(user.getEmail());
        appUser.setBannerImage(image.getImageLink());
        appUserService.save(appUser);
        return new ResponseEntity<>(image,HttpStatus.OK);
    }
}
