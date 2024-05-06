package com.example.backendmainserver.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtProvider {

    private final String AUTHORIZATION_ROLE = "authorities";

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-expiration-seconds}")
    private int accessExpirationSeconds;

    @Value("${jwt.refresh-expiration-seconds}")
    private int refreshExpirationSeconds;

    /**
     * access token 생성
     */
    public String createAccessToken(Long userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofSeconds(accessExpirationSeconds).toMillis());



        return Jwts.builder()
                .claim("memberId", userId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(createKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * refresh token 생성
     */
    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofSeconds(refreshExpirationSeconds).toMillis());



        return Jwts.builder()
                .claim("memberId", userId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(createKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰 정보 추출
     */
//    public Authentication toAuthentication(String token) {
//
//        Claims claims = validate(token).getBody();
//
//        Object roles = claims.get(AUTHORIZATION_ROLE);
//        Set<GrantedAuthority> authorities = null;
//        if (roles != null && !roles.toString().trim().isEmpty()) {
//            authorities = Set.of(new SimpleGrantedAuthority(roles.toString()));
//        }
//        UserDetails user = UserDetailsImpl.builder()
//                .memberId(claims.get("memberId", Long.class))
//                .name(claims.get("name", String.class))
//                .authorities(authorities)
//                .build();
//
//        return new UsernamePasswordAuthenticationToken(user, token, authorities);
//    }


    /**
     * 토큰 검증
     */
    public Jws<Claims> validate(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(createKey())
                .build();
        return jwtParser.parseClaimsJws(token);
    }

    private Key createKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }


}
