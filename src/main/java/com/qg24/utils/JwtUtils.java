package com.qg24.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    //密钥
    public static final Key secretKey = Keys.hmacShaKeyFor("sf6RPMOtrvIkoiYiErJJpaPUiBX9Y72quznqj1tafcc=".getBytes(StandardCharsets.UTF_8));

    /**
     * 判断令牌是否过期
     * @param token
     * @return
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            // 如果解析失败或签名无效，也视为过期
            return true;
        }
    }

    /**
     * 生成令牌
     * @param claims
     * @return
     */
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(secretKey)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .compact();
    }


    /**
     * 解析 JWT 并返回声明
     * @param token JWT 字符串
     * @return Claims 对象
     */
    public static Claims parseJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
