package com.krenog.myf.security.jwt;

import com.krenog.myf.security.detail.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtProvider {
    private static final Logger logger = LogManager.getLogger(JwtProvider.class);
    private final JwtConfig jwtConfig;

    public JwtProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateJwtToken(UserPrincipal userPrincipal) {
        Map<String, Object> privateClaims = new HashMap<>();
        privateClaims.put(jwtConfig.getUserIdClaimKey(), userPrincipal.getId());
        privateClaims.put(jwtConfig.getUserAuthoritiesClaimKey(), userPrincipal.getAuthorities());

        return Jwts.builder()
                // Add private claims (this needs to come first or it will override everything else)
                .setClaims(privateClaims)
                // Random ID generated
                .setId(UUID.randomUUID().toString())
                // App is the issuer and also intended audience. User is principal subject
                .setIssuer(jwtConfig.getAppName())
                .setAudience(jwtConfig.getAppName())
                .setSubject(userPrincipal.getUsername())
                // Setup timestamp and validity
                .setIssuedAt(new Date())
                .setNotBefore(new Date())
                .setExpiration(generateExpirationDate())
                // Sign and generate token
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
                .compact();
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + jwtConfig.getExpiration() * 1000);
    }

    String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    UserPrincipal parseToken(String token) {
        // First, validate token
        if (!validateJwtToken(token)) {
            throw new IllegalArgumentException("JWT not valid : " + token);
        }

        // Extract claims
        Jws<Claims> jwt = Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token);
        Claims claims = jwt.getBody();

        // Build User from JWT
//        Set<GrantedAuthority> grantedAuthorities = (Set<GrantedAuthority>)((List)claims.get(userAuthoritiesClaimKey)).stream()
//                .map(elem -> ((Map)elem).get("authority"))
//                .map(authority -> new SimpleGrantedAuthority((String)authority))
//                .collect(Collectors.toSet());
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new UserPrincipal(Long.valueOf((Integer) claims.get(jwtConfig.getUserIdClaimKey())), claims.getSubject(),
                "", authorities);
    }


    Boolean validateJwtToken(String token) {
        try {
            // Check JWT signature and audience
            Jws<Claims> jwt = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret())
                    .requireAudience(jwtConfig.getAppName())
                    .parseClaimsJws(token);

            // Make sure JWT is still valid  (notBefore < now < expiration)
            return jwt.getBody().getNotBefore().before(new Date()) && jwt.getBody().getExpiration().after(new Date());
        } catch (Exception ex) {
            logger.error("JWT not properly signed or with wrong audience : " + token);
            return false;
        }
    }
}
