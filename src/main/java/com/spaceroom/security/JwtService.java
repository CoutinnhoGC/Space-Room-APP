package com.spaceroom.security;

import com.spaceroom.entities.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class JwtService {

    private final String secret;
    private final long expirationMinutes;
    private SecretKey signingKey;

    public JwtService(@Value("${app.security.jwt.secret}") String secret,
                      @Value("${app.security.jwt.expiration-minutes}") long expirationMinutes) {
        this.secret = secret;
        this.expirationMinutes = expirationMinutes;
    }

    @PostConstruct
    void init() {
        byte[] secretBytes = secret.length() >= 32
                ? secret.getBytes(StandardCharsets.UTF_8)
                : Decoders.BASE64.decode(secret);
        this.signingKey = Keys.hmacShaKeyFor(secretBytes);
    }

    public String generateToken(Usuario usuario) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        return Jwts.builder()
                .subject(String.valueOf(usuario.getIdUsuario()))
                .issuedAt(Date.from(now.toInstant(ZoneOffset.UTC)))
                .expiration(Date.from(now.plusMinutes(expirationMinutes).toInstant(ZoneOffset.UTC)))
                .claim("institutionId", usuario.getIdInstituicao())
                .claim("email", usuario.getEmail())
                .signWith(signingKey)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public LocalDateTime getExpirationDate(String token) {
        return LocalDateTime.ofInstant(parseClaims(token).getExpiration().toInstant(), ZoneOffset.UTC);
    }
}
