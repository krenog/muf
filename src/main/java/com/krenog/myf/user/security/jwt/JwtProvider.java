package com.krenog.myf.user.security.jwt;

import com.krenog.myf.user.security.detail.UserDetailServiceImpl;
import com.krenog.myf.user.security.detail.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtProvider {
    private static final Logger logger = LogManager.getLogger(JwtProvider.class);
    private final JwtConfig jwtConfig;
    @Autowired
    UserDetailServiceImpl userDetailService;

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

    UserDetails parseToken(String token) {
        if (!validateJwtToken(token)) {
            throw new IllegalArgumentException("JWT not valid : " + token);
        }
        // Extract claims
        Jws<Claims> jwt = Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token);
        Claims claims = jwt.getBody();
        return userDetailService.loadById(Long.valueOf((Integer) claims.get(jwtConfig.getUserIdClaimKey())));
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
