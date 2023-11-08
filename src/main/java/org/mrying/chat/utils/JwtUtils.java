package org.mrying.chat.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * @author 邓和颖
 * @since 2023/10/11 9:20
 */

@Slf4j
public class JwtUtils {

    // 密钥
    private final String secret;

    // 过期时间
    private final Long expiration;

    public JwtUtils(String secret, Long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    // 根据负载生成 jwt token
    public String createToken(Map<String, Object> claims) {

        // 用于在 JWT（JSON Web Token）的签名和验证过程中使用 HMAC-SHA 签名算法
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        // jjwt 构建 jwt builder
        // setId 设置JWT令牌的唯一标识符
        // setClaims 将准备好的声明信息全部设置到 jwt 中，再次设置即覆盖
        // setIssuedAt 设置令牌发放时间
        // setExpiration 设置令牌过期时间
        // signWith 设置签名
        // compact 将 JWT 令牌转换为字符串表示
        return Jwts.builder()
                .setId(UUIDUtils.getUUID())
                .setClaims(claims)
                // JWT的签发者
                .setIssuer("Chat")
                .setIssuedAt(new Date())
                .setExpiration(expirationDate())
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    // 根据 token 获取用户名
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = (String) claims.get("phone");
        } catch (Exception e) {
            username = null;
            log.info("error:{}", "用户名未能获取 from token");
        }
        return username;
    }

    // 从 token 中获取荷载
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            e.printStackTrace();
        }
        return claims;
    }

    // 生成 token 失效时间
    private Date expirationDate() {
        // 失效时间为：系统当前毫秒数+我们设置的时间（s）*1000=>毫秒
        // 其实就是未来 7 天
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    // 判断 token 是否有效
    public boolean validateToken(String token, Map<String, Object> claims) {
        // 判断 token 是否过期
        // 判断 token 是否和 userDetails 中的一致
        // 我们要做的是先获取用户名
        String username = getUsernameFromToken(token);
        return username.equals(claims.get("name")) && isTokenExpired(token);
    }

    // 判断 token、是否失效
    // 失效返回 true
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFeomToken(token);
        return !expiredDate.before(new Date());
    }

    // 从荷载中获取时间
    private Date getExpiredDateFeomToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }
}
