package com.carwash.users.jwtimpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    public final String SECRET_KEY="theonepieceisrealgoldrogergofind";

    private byte[] getSigningKey(){
        return Decoders.BASE64.decode(SECRET_KEY);
    }

    //Method to generate JWT token
    public String generateToken(String email){
        Map<String,Object> claims=new HashMap<>();

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*60*10))  //10 hours expiration time
                .signWith(Keys.hmacShaKeyFor(getSigningKey()), SignatureAlgorithm.HS256)
                .compact();
    }

    // to extract email from the JWT token
    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    //to extract any claim from the token
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(getSigningKey()))  // Correct signing key method
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



}
