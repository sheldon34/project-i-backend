package com.example.securityskilltesting.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTGenerator {
    public String generateToken(Authentication authentication){

        String username=authentication.getName();
        Date currentDate=new Date();
        Date expiryDate= new Date(currentDate.getTime()+SecurityConstants.JWT_EXPIRATION);
///  seting up the token
    String token= Jwts.builder()
            .setSubject(username)
            .setIssuedAt(currentDate)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512,SecurityConstants.JWT_SECRET)
            .compact();

return token;
    }
    //geting and validating data from the user token

    public String getUsernameFromJWT(String token){
        Claims claims= Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    //validating the token
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("jwt was incorrect or exipired");
        }
    }
}
