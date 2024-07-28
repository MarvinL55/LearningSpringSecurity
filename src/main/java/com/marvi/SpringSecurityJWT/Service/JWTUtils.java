package com.marvi.SpringSecurityJWT.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
// Utility class for generating and validating JWT token
@Component
public class JWTUtils {
    
    private SecretKey key; // Secrete key for signing JWT token
    private static final long EXPIRATION_TIME = 86400000; // Token expiration time
    
    // Constructor to initialize the secret key
    public JWTUtils(){
        String secretString = "93984656969679499736579367946796937567946579463976397657946975696BHDBHB659364793465746T3GVJC"; // Secret key String
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8)); // Decode the secret key string
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256"); // Create SecretKey object 
    }

    // Method to generate JWT token
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
            .subject(userDetails.getUsername()) // Set token subject (username)
            .issuedAt(new Date(System.currentTimeMillis())) // Set token issue date
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Set token expiration date
            .signWith(key) // Sign the token with the secret key
            .compact(); // Build the token
    }

    // Method to generate JWT refresh token
    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails){
        return Jwts.builder()
            .claims(claims) // Set claims in the token 
            .subject(userDetails.getUsername()) // Set token issue date 
            .issuedAt(new Date(System.currentTimeMillis())) // Set token issue date
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //Set token expiration date
            .signWith(key) // Sign the token with the secret key
            .compact(); // validate token
    }

    // Method to extract username from token
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject); // Extract subject(username) from the token 
    }

    // Method to extract claims from token 
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload()); // Parse and extract claims from token
    }

    // Method to check if the token is valid 
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token); // Extract username from token
        return(username.equals(userDetails.getUsername())&&!isTokenExpired(token)); // Checks if the token is valid and not expired
    }

    // Method to check if the token is expired 
    public boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date()); // Checks if the token expiration date is the current date
    }
}
