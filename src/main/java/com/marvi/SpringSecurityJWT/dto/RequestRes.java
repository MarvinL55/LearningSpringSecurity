package com.marvi.SpringSecurityJWT.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.marvi.SpringSecurityJWT.Entity.OurUsers;
import com.marvi.SpringSecurityJWT.Entity.Products;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestRes {
    
    private int statusCode; // Http status code
    private String error; // Error message
    private String message; // Success message
    private String token; // JWT token
    private String refreshToken; // JWT refresh token
    private String expirationTime; // Token expiration time
    private String name; // Product or user name
    private String email; // User email
    private String role; // Users role
    private String password; // User password
    private List<Products> product; // List of products
    private OurUsers ourUsers; // User detail
}
