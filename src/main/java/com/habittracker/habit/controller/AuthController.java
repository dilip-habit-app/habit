package com.habittracker.habit.controller;

import com.habittracker.habit.service.JWTService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private JWTService jwtService;
}
