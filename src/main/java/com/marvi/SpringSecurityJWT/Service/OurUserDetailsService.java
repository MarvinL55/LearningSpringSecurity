package com.marvi.SpringSecurityJWT.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.marvi.SpringSecurityJWT.Repository.OurUserRepo;
// Service for loading user details by username 
@Service
public class OurUserDetailsService implements UserDetailsService{

    @Autowired
    private OurUserRepo ourUserRepo; // Repository to manage user data

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        return ourUserRepo.findByEmail(username).orElseThrow(); // Finds user by email
    }
    
}
