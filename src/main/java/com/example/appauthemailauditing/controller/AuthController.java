package com.example.appauthemailauditing.controller;

import com.example.appauthemailauditing.payload.ApiResponse;
import com.example.appauthemailauditing.payload.RegisterDto;
import com.example.appauthemailauditing.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService service;

    @PostMapping("/register")
    public HttpEntity<?> registerUser(@RequestBody RegisterDto dto) {
        ApiResponse apiResponse = service.registerUser(dto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String email, @RequestParam String emailCode) {
        ApiResponse apiResponse = service.verifyEmail(email, emailCode);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody RegisterDto dto) {
        ApiResponse apiResponse = service.login(dto);
        return ResponseEntity.status(apiResponse != null ? 200 : 401).body(apiResponse);
    }
}
