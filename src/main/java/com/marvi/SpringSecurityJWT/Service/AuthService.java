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

    // Method that handles user sign in
    public RequestRes signUp(RequestRes registrationRequest){
        RequestRes resp = new RequestRes();
        try{
            OurUsers ourUsers = new OurUsers();
            ourUsers.setEmail(registrationRequest.getEmail()); // set user email
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword())); // Encode ans et user password
            ourUsers.setRole(registrationRequest.getRole());// Set user role
            OurUsers ourUserResults = ourUserRepo.save(ourUsers); // Saves use to the repo
            if(ourUserResults != null && ourUserResults.getId() > 0){
                resp.setOurUsers(ourUserResults); // Set the response of the user details
                resp.setMessage("User Saved Successfully"); // Set success message
                resp.setStatusCode(200); // Set success error message
            }
        }catch(Exception e){
            resp.setStatusCode(500); // Set status error code
            resp.setError(e.getMessage()); // Set error message
        }
        return resp;
    }

    // Method to handle user sign-in
    public RequestRes signIn(RequestRes signRequestRes){
        RequestRes response = new RequestRes();

        try{
            // Authenticate the user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signRequestRes.getEmail(), signRequestRes.getPassword()));
            // Finds teh user by the email
            var user = ourUserRepo.findByEmail(signRequestRes.getEmail()).orElseThrow();
            // Prints the user
            System.out.println("USER IS: " + user);
            // Generate JWT token
            var jwt = jwtUtils.generateToken(user);
            // Generate the refresh token
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200); // Set success status code
            response.setToken(jwt); // set jwt token in response
            response.setRefreshToken(refreshToken); // Set refresh token in response
            response.setExpirationTime("24hr");// set token expiration time
            response.setMessage("Successfully"); // Set success message
        }catch(Exception e){
            response.setStatusCode(500); // set Status code
            response.setError(e.getMessage()); // set error message 
        }
        return response;
    }

    // Method to handle token refresh
    public RequestRes refreshToken(RequestRes refreshTokenRequest){
        RequestRes response = new RequestRes();
        // Extract username from token
        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        // Find user by id
        OurUsers users = ourUserRepo.findByEmail(ourEmail).orElseThrow();
        // Validate token
        if(jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)){
            // Generate token 
            var jwt = jwtUtils.generateToken(users);
            // Set status code
            response.setStatusCode(200);
            // set the new jwt token in the response
            response.setToken(jwt);
            // Set refresh token
            response.setRefreshToken(refreshTokenRequest.getToken());
            // Set the token refresh time
            response.setExpirationTime("24hr");
            // Set success message
            response.setMessage("Successfully Refreshed token");
        }
        // Error status
        response.setStatusCode(500);
        return response;
    }
}