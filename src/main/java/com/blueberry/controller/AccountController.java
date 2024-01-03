package com.blueberry.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts/api/auth")
@CrossOrigin("*")
@AllArgsConstructor
public class AccountController {
}
