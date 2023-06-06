package com.ecom.user.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String SECRET_KEY = "73357638792F423F4428472B4B6250655368566D597133743677397A24432646";

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject); // subject is the email in your jwt token
    }

    // extract a claim from all the claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))  // to keep the information when the token i generated so that it can be checked whrn to expire the token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))  // token expiration time after issued
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // method to check if the token belongs to the corresponding user
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // method to check if token is expired or not
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
        .setSigningKey(getSignInKey())  // signinkey is the secret_key in your token which identifies the user who he claims to be 
        .build()
        .parseClaimsJws(token)
        .getBody();
    }

    private Key getSignInKey() {
        byte[] keyByes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByes);
    }

}
