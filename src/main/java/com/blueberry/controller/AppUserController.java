package com.blueberry.controller;

import com.blueberry.model.acc.User;
import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.Image;
import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.model.dto.MessageResponse;
import com.blueberry.model.dto.UserDetails;
import com.blueberry.model.request.AppUserRequest;
import com.blueberry.model.request.UserRequest;
import com.blueberry.service.AppUserService;
import com.blueberry.service.UserService;
import com.blueberry.util.ModelMapperUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth/api/appusers")
@CrossOrigin("*")
@AllArgsConstructor
public class AppUserController {
    private AppUserService appUserService;
    private ModelMapperUtil modelMapper;
    private UserService userService;


    @GetMapping
    public ResponseEntity<Iterable<AppUserDTO>> getUsers(){
        List<AppUser> appUsers = (List<AppUser>) appUserService.findAll();
        return new ResponseEntity<>(modelMapper.mapList(appUsers,AppUserDTO.class),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDetails> getAppUser(@PathVariable Long id) {
        Optional<AppUser> appUser = appUserService.findById(id);
        if (appUser.isPresent()) {
            UserDetails userDetails= modelMapper.map(appUser.get(),UserDetails.class);
            return new ResponseEntity<>(userDetails,HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/current-user")
    public ResponseEntity<AppUserDTO> getCurrentAppUser(){
        AppUser appUser = appUserService.getCurrentAppUser();
       AppUserDTO appUserDTO= modelMapper.map(appUser,AppUserDTO.class);
        return new ResponseEntity<>(appUserDTO,HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppUser(@PathVariable Long id, @RequestBody AppUserRequest userRequest) {
        Optional<AppUser> user = appUserService.findById(id);
        AppUser currentUser = appUserService.getCurrentAppUser();
        if (user.isPresent()) {
            if(!user.get().getUser().getEmail().equals(currentUser.getUser().getEmail())){
                return new ResponseEntity<>(new MessageResponse("Unauthorized"),HttpStatus.FORBIDDEN);
            }
            AppUser appUserOld= user.get();
            appUserOld.setDob(userRequest.getDob());
            appUserOld.setPhoneNumber(userRequest.getPhoneNumber());
            appUserOld.setFirstName(userRequest.getFirstName());
            appUserOld.setLastName(userRequest.getLastName());
            appUserOld.setHobbies(userRequest.getHobbies());
            appUserOld.setAddress(userRequest.getAddress());
            appUserService.save(appUserOld);
            return new ResponseEntity<>(modelMapper.map(appUserOld,UserDetails.class),HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse(""),HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/change-avatar")
    public ResponseEntity<Image> changeAvatar(@RequestBody Image image){
        AppUser appUser = appUserService.getCurrentAppUser();
        appUser.setAvatarImage(image.getImageLink());
        appUserService.save(appUser);
        return new ResponseEntity<>(image,HttpStatus.OK);
    }
    @PatchMapping("/change-banner")
    public ResponseEntity<Image> changeBanner(@RequestBody Image image){
        AppUser appUser = appUserService.getCurrentAppUser();
        appUser.setBannerImage(image.getImageLink());
        appUserService.save(appUser);
        return new ResponseEntity<>(image,HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String query){
        List<AppUser> appUsers = (List<AppUser>) appUserService.findByName(query,false);
        return new ResponseEntity<>(modelMapper.mapList(appUsers,AppUserDTO.class),HttpStatus.OK);
    }
}
