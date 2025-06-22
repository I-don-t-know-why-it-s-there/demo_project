package com.example.demo.global.util;

import com.example.demo.global.constant.Const;
import com.example.demo.global.enums.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        try {
            if (!StringUtils.hasText(secretKey)) {
                throw new IllegalArgumentException("JWT 시크릿 키가 설정되지 않았습니다.");
            }

            byte[] bytes = Base64.getDecoder().decode(secretKey);
            if (bytes.length < 32) {
                throw new IllegalArgumentException("JWT 시크릿 키는 최소 32바이트 이상이어야 합니다.");
            }

            key = Keys.hmacShaKeyFor(bytes);
        } catch (IllegalArgumentException e) {
            log.error("JWT 시크릿 키 초기화 실패: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "JWT 설정 오류");
        }
    }

    public String createToken(Long userId, String email, UserRole userRole) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Const.TOKEN_VALIDITY_IN_MS);

        return Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("email", email)
                        .claim("userRole", userRole)
                        .setExpiration(expiryDate)
                        .setIssuedAt(now)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public String substringToken(String tokenValue) {
        if (!StringUtils.hasText(tokenValue)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다.");
        }
        if (!tokenValue.startsWith(Const.Token_Prefix)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 토큰 형식입니다.");
        }
        return tokenValue.replace(Const.Token_Prefix, "");
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.");
        } catch (SecurityException | MalformedJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 잘못되었습니다.");
        }
    }

    public Long getUserId(String token) {
        try {
            String userId = extractClaims(token).getSubject();
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 ID 형식입니다.");
        }
    }

    public UserRole getUserRole(String token) {
        try {
            String userRole = extractClaims(token).get("userRole", String.class);
            return UserRole.of(userRole);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 사용자 권한입니다.");
        }
    }

    public String getEmail(String token) {
        String userEmail = extractClaims(token).get("email", String.class);
        if (userEmail == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 정보가 없습니다.");
        }
        return userEmail;
    }
}