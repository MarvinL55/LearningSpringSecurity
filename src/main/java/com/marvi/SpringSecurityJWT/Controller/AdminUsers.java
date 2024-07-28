package com.marvi.SpringSecurityJWT.Controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.marvi.SpringSecurityJWT.Entity.Products;
import com.marvi.SpringSecurityJWT.Repository.ProductsRepo;
import com.marvi.SpringSecurityJWT.dto.RequestRes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class AdminUsers {
    
    @Autowired
    private ProductsRepo productsRepo;

    @GetMapping("/public/products")
    public ResponseEntity<Object> getAllProducts() {
        return ResponseEntity.ok(productsRepo.findAll());
    }

    @PostMapping("/admin/saveproducts")
    public ResponseEntity<Object> signup(@RequestBody RequestRes productRequest) {  
        Products productsToSave = new Products();
        productsToSave.setName(productRequest.getName());
        return ResponseEntity.ok(productsRepo.save(productsToSave));
    }

    @GetMapping("/user/alone")
    public ResponseEntity<Object> userAlone() {
        return ResponseEntity.ok("Users alone can access this Api only");
    }

    @GetMapping("/adminuser")
    public ResponseEntity<Object> bothAdminAndUsersApi() {
        return ResponseEntity.ok("Both users and admin can access this");
    }
}
