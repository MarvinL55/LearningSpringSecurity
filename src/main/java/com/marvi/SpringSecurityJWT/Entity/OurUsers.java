package com.marvi.SpringSecurityJWT.Entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.*;

@Data
@Entity
@Table(name = "ourusers")
public class OurUsers implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // user ID
    private String email; // User email
    private String password; // user password
    private String role; // User role

	@Override
	public String getUsername() {
		
		return email; // Return email as username 
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return List.of(new SimpleGrantedAuthority(role)); // Return user role as authority
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // Account is not expired
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // Credentials are not expired 
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Credentials are not expired
	}

	@Override
	public boolean isEnabled() {
		return true; // Account is enabled
	}
}
