package com.marvi.SpringSecurityJWT.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marvi.SpringSecurityJWT.Service.AuthService;
import com.marvi.SpringSecurityJWT.dto.RequestRes;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    // Endpoint for user registration
    @PostMapping("/signup")
    public ResponseEntity<RequestRes> signup(@RequestBody RequestRes signUpRequest) {  
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }
    
    // Endpoint for user user login
    @PostMapping("/signin")
    public ResponseEntity<RequestRes> signIn(@RequestBody RequestRes signInRequest) {  
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }

    // Endpoint for refresh JWT token
    @PostMapping("/refresh")
    public ResponseEntity<RequestRes> refresh(@RequestBody RequestRes refreshTokenRequest) {  
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
}
