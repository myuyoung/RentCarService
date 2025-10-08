package me.changwook.configuration.config.security;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter
@Setter
@Slf4j
public class JwtUtil {

    private final SecretKey key;
    private final long expiration;
    private final long refreshInterval;

    public JwtUtil(
            @Value("${jwt.secret}")String secretKey,
            @Value("${jwt.expire}")long expiration,
            @Value("${jwt.refresh-expire}")long refreshInterval) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.expiration = expiration;
        this.refreshInterval = refreshInterval;
    }

    public String generateAccessToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .subject(username)
                .claim("role", role)  // 권한 정보 추가
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshInterval);
        return Jwts.builder()
                .subject(username)
                .claim("role", role)  // 권한 정보 추가
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        }catch (ExpiredJwtException e){
            throw e;
        }catch (SignatureException | MalformedJwtException e){
            log.warn("서명 오류나 형식이 잘못된 토큰입니다.");
            return false;
        }
        catch (Exception e) {
            log.error("JWT 토큰 검증에서 에러가 발생했습니다 {}",e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration();
    }

    public String getRoleFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public String generateAccessToken(String username) {
        return generateAccessToken(username, "ROLE_USER");
    }

    public String generateRefreshToken(String username) {
        return generateRefreshToken(username, "ROLE_USER");
    }

    // Getter 메서드 추가
    public long getExpiration() {
        return expiration;
    }

}
