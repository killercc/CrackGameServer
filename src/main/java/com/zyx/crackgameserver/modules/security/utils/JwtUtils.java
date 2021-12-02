package com.zyx.crackgameserver.modules.security.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {

    private long expire;
    private String secretKey;
    private String header;



    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

//    public Date extractExpiration(String token){
//        return extractClaim(token,Claims::getExpiration);
//    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

//    private Boolean isTokenExpired(String token){
//        return extractExpiration(token).before(new Date());
//    }

    public String generateToken(UserDetails userDetail){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,userDetail.getUsername());
    }
    private String createToken(Map<String,Object> claims,String subject){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //不设置token过期时间 交给redis维护
                //.setExpiration(new Date(System.currentTimeMillis() + 1000 * expire))
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean validateToken(String token,UserDetails userDetails){
        final String username = extractUsername(token);
        return !(username.equals(userDetails.getUsername()));// && !isTokenExpired(token)
    }



//    /**
//     * 生成 jwt
//     * @param username
//     * @return
//     */
//    public String generateToken(String username){
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        Key key = Keys.hmacShaKeyFor(keyBytes);
//        Date nowDate = new Date();
//        Date expireDate = new Date(nowDate.getTime() + 1000 * expire);
//
//        return Jwts.builder()
//                .setHeaderParam("typ","JWT")
//                .setSubject(username)
//                .setIssuedAt(nowDate)
//                .setExpiration(expireDate)
//                .signWith(key,SignatureAlgorithm.HS512)
//                .compact();
//    }
//
//
//    /**
//     * 解析jwt
//     * @param jwt
//     * @return
//     */
//    public Claims getClaimByToken(String jwt){
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(jwt)
//                .getBody();
//    }
//
//
//    /**
//     * 判断token是否过期
//     * @param claims
//     * @return
//     */
//    public boolean isTokenExpired(Claims claims){
//        return claims.getExpiration().before(new Date());
//    }
}
