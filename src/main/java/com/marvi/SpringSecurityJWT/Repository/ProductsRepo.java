package com.marvi.SpringSecurityJWT.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marvi.SpringSecurityJWT.Entity.*;
// Product repository
public interface ProductsRepo extends JpaRepository<Products, Long>{
    
}
