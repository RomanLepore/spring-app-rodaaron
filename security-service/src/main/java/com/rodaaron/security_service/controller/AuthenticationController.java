package com.rodaaron.security_service.controller;

import com.rodaaron.security_service.controller.dto.AuthCreateUserRequest;
import com.rodaaron.security_service.controller.dto.AuthLoginRequest;
import com.rodaaron.security_service.controller.dto.AuthResponse;
import com.rodaaron.security_service.persistence.entities.UserEntity;
import com.rodaaron.security_service.services.UserDetailServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest loginRequest){
        return new ResponseEntity<>(this.userDetailService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody @Valid AuthCreateUserRequest authCreateUserRequest){
            return new ResponseEntity<>(this.userDetailService.createUser(authCreateUserRequest), HttpStatus.OK);
    }

    @GetMapping("/get")
    public int getPrueba(){
        return 123;
    }
}
