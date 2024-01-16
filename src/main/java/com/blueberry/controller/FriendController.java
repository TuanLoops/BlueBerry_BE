package com.blueberry.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/api/friends")
@CrossOrigin("*")
@AllArgsConstructor
public class FriendController {

    @GetMapping
    public ResponseEntity<?> getAllFriends(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> createFriendRequest(@RequestBody Long id){

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
