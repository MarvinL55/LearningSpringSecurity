package com.marvi.SpringSecurityJWT.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marvi.SpringSecurityJWT.Entity.OurUsers;
// Repository interface for managing ourUser entity
public interface OurUserRepo extends JpaRepository<OurUsers, Long>{

    Optional<OurUsers> findByEmail(String email); // Method to find the user by email 
}
