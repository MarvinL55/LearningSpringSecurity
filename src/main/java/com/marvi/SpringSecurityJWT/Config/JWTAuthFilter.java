package com.marvi.SpringSecurityJWT.Config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.marvi.SpringSecurityJWT.Service.JWTUtils;
import com.marvi.SpringSecurityJWT.Service.OurUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Custom filter to validate the JWT token in the incoming request
@Component
public class JWTAuthFilter extends OncePerRequestFilter{

    @Autowired
    private JWTUtils jwtUtils; // Utility class for handling the JWT operations

    @Autowired
    private OurUserDetailsService ourUserDetailsService; // Service to load user-specific

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); // Get authorization header from the request
        final String jwtToken;
        final String userEmail;

        // Check if the authentication header is present and starts with bearer
        if(authHeader == null || authHeader.isBlank()){
            filterChain.doFilter(request, response); // Continue filter chain if header is missing or invalid
            return;
        }
        
        // Extract the JWT token from the authorization header "bearer"
        jwtToken = authHeader.substring(7);
        // Extract the username(email) from token using utility class
        userEmail = jwtUtils.extractUsername(jwtToken);
        // Validate the token ad set the authentication in the security context
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // Load user details the user detail service
            UserDetails userDetails = ourUserDetailsService.loadUserByUsername(userEmail);

            // Checks if the token is valid
            if(jwtUtils.isTokenValid(jwtToken, userDetails)){
                // Create a new empty security context
                SecurityContext SecurityContext = SecurityContextHolder.createEmptyContext(); 
                // Create a new authentication token with the user details and authorities
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // Set additional details for the authentication token
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Set the authentication detail for the security context 
                SecurityContext.setAuthentication(token); 
                // Set the SecurityContext in the SecurityContextHolder
                SecurityContextHolder.setContext(SecurityContext);
            }
        }
        // Continue th filter chain 
        filterChain.doFilter(request, response); 
    }
}
