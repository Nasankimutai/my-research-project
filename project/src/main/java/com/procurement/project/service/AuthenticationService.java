package com.procurement.project.service;

import com.procurement.project.model.Authentication;
import com.procurement.project.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Authentication registerUser(String email, String password, String role) {
        if (authenticationRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        Authentication user = Authentication.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(new HashSet<>() {{
                    add(role);
                }})
                .build();

        return authenticationRepository.save(user);


    }
}
