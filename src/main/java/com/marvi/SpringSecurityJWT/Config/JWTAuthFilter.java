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
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        if(authHeader == null || authHeader.isBlank()){
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = authHeader.substring(7);
        userEmail = jwtUtils.extractUsername(jwtToken);
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = ourUserDetailsService.loadUserByUsername(userEmail);

            if(jwtUtils.isTokenValid(jwtToken, userDetails)){
                SecurityContext SecurityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext.setAuthentication(token);
                SecurityContextHolder.setContext(SecurityContext);
            }
        }
        filterChain.doFilter(request, response);
    }
}
