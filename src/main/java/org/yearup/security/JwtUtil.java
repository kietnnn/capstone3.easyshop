package org.yearup.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.yearup.models.User;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil
{
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours

    public String generateToken(User user)
    {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token)
    {
        return parseToken(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token)
    {
        try
        {
            parseToken(token);
            return true;
        }
        catch (JwtException e)
        {
            return false;
        }
    }

    private Jws<Claims> parseToken(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
