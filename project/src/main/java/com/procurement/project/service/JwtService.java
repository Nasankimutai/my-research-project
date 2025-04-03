package com.procurement.project.service;

import com.procurement.project.model.Authentication;
import com.procurement.project.repository.AuthenticationRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtService {

    // Make sure the secret is at least 64 characters (512 bits)
    private static final String SECRET_KEY = "my-very-long-secret-key-that-is-surely-longer-than-64-characters-indeed";
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticate(String email, String password) {
        Optional<Authentication> user = authenticationRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return generateToken(email, user.get().getRoles());
        } else {
            throw new RuntimeException("Invalid email or password");
        }
        
        
    }

    private String generateToken(String email, Set<String> roles) {
        // Convert the plain text secret to a SecretKey instance
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
