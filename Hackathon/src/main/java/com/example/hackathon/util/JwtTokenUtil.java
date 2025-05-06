package com.sparkyconsulting.aihub.util;

import com.sparkyconsulting.aihub.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private final JwtConfig config;
    private final Key key;

    public JwtTokenUtil(JwtConfig config) {
        this.config = config;
        this.key = Keys.hmacShaKeyFor(config.getSecret().getBytes());
    }

    public String generateToken(String username, String role, Long companyId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("companyId", companyId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + config.getExpiration()))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Long getCompanyId(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody().get("companyId", Long.class);
    }

    public String getRole(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody().get("role", String.class);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
