package com.example.appauthemailauditing.secutiry;

import com.example.appauthemailauditing.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {
    private static final long expireTime = 1000 * 60 * 60 * 24;
    String secretKey = "maxviySoz";

    public String generateToken(String email, Set<Role> roles) {
        Date expireDate = new Date(System.currentTimeMillis() + expireTime);

        return Jwts
                .builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getEmailFromToken(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)      // nimani parse qilay
                    .getBody()                  // qayerdan
                    .getSubject();              // nimasini olay
        } catch (Exception e) {
            return null;
        }
    }
}
