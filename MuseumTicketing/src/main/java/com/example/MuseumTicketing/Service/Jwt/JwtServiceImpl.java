package com.example.MuseumTicketing.Service.Jwt;

import com.example.MuseumTicketing.DTO.AdminScanner.JwtAuthenticationResponse;
import com.example.MuseumTicketing.Model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.security.Key;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    public String generateToken(UserDetails userDetails){

        String role = getUserRole(userDetails);

        return Jwts.builder().setSubject(userDetails.getUsername())
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date((System.currentTimeMillis() + 1000*60*60*24*365)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date((System.currentTimeMillis() + 1000*60*60*12)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtAuthenticationResponse generateTokenAndFlag(UserDetails userDetails) {
        String token = generateToken(userDetails);
       // String role = extractUserRoleFromToken(token);
        boolean isAdmin = checkIfAdmin(userDetails);
        String sessionId = userDetails.getUsername();
        log.info("User Authorities: " + userDetails.getAuthorities());

        //boolean isAdmin = role.equalsIgnoreCase("ADMIN");

        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setToken(token);
        response.setAdmin(isAdmin);
        response.setSessionId(sessionId);
        return response;
    }

    private String extractUserRoleFromToken(String token) {
        Claims claims = extractAllClaims(token);
        String role = (String) claims.get("role"); // Extract role from claims

        log.info("Extracted Role: " + role);

        return role;
    }

    private boolean checkIfAdmin(UserDetails userDetails) {
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.getAuthority().equalsIgnoreCase(Role.ADMIN.name())) {
                return true; // User is admin
            }
        }
        return false; // User is not admin
    }




    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSignInKey(){
        byte[] key = Decoders.BASE64.decode("HACqcd4WllFFL84rCcLJJsDuPJCYZB3IsjTlYmmrU2w7plbEV4xOqc0BJdIMPpqy");
        return Keys.hmacShaKeyFor(key);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private String getUserRole(UserDetails userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equalsIgnoreCase(Role.ADMIN.name()))) {
            return "ADMIN";
        } else if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equalsIgnoreCase(Role.EMPLOYEE.name()))) {
            return "EMPLOYEE";
        } else if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equalsIgnoreCase(Role.SCANNER.name()))) {
            return "SCANNER";
        } else {
            return "GUEST";
        }
    }
}
