package com.marvi.SpringSecurityJWT.Config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.marvi.SpringSecurityJWT.Service.OurUserDetailsService;
// Configuration class for setting up the security with spring security.
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private OurUserDetailsService ourUserDetailsService; // Service to load user-specific data
    @Autowired
    private JWTAuthFilter jwtAuthFilter; // Custom JWT authentication filter 

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
            .authorizeHttpRequests(request -> request.requestMatchers("/auth/**", "/public/**").permitAll() // All unauthenticated access
                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")// Restricts access to the admin endpoint
                .requestMatchers("/user/**").hasAnyAuthority("USER") // Restricted access to the user endpoint
                .requestMatchers("/adminuser/**").hasAnyAuthority("USER", "ADMIN") // Allow access to users and admin
                .anyRequest().authenticated()) // Requires authentication fro any other requests
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless session management
                .authenticationProvider(authenticationProvider()).addFilterBefore( // Set authentication provider
                    jwtAuthFilter, UsernamePasswordAuthenticationFilter.class // Add JWT authentication filter before the username/password authentication filter
                );
        return httpSecurity.build(); // Builds the security filter chain
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(ourUserDetailsService); // Set the user detail service
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder()); //set the password encoder
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new  BCryptPasswordEncoder(); // Use BCrypt password encoder
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Get authentication manager from the configuration
    }
}
