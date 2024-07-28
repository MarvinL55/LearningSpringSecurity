package com.marvi.SpringSecurityJWT.Service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.marvi.SpringSecurityJWT.Entity.OurUsers;
import com.marvi.SpringSecurityJWT.Repository.OurUserRepo;
import com.marvi.SpringSecurityJWT.dto.RequestRes;

@Service
public class AuthService {
    
    @Autowired
    private OurUserRepo ourUserRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public RequestRes signUp(RequestRes registrationRequest){
        RequestRes resp = new RequestRes();
        try{
            OurUsers ourUsers = new OurUsers();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRole(registrationRequest.getRole());
            OurUsers ourUserResults = ourUserRepo.save(ourUsers);
            if(ourUserResults != null && ourUserResults.getId() > 0){
                resp.setOurUsers(ourUserResults);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        }catch(Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public RequestRes signIn(RequestRes signRequestRes){
        RequestRes response = new RequestRes();

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signRequestRes.getEmail(), signRequestRes.getPassword()));
            var user = ourUserRepo.findByEmail(signRequestRes.getEmail()).orElseThrow();
            System.out.println("USER IS: " + user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24hr");
            response.setMessage("Successfully");
        }catch(Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public RequestRes refreshToken(RequestRes refreshTokenRequest){
        RequestRes response = new RequestRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        OurUsers users = ourUserRepo.findByEmail(ourEmail).orElseThrow();
        if(jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)){
            var jwt = jwtUtils.generateToken(users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenRequest.getToken());
            response.setExpirationTime("24hr");
            response.setMessage("Successfully Refreshed token");
        }
        response.setStatusCode(500);
        return response;
    }
}