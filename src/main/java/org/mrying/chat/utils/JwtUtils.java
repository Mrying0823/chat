package org.mrying.chat.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.mrying.chat.constants.Constants;
import org.yaml.snakeyaml.scanner.Constant;

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

    // 获取转换后的私钥对象
    public SecretKey getSecretKey() {
        // 用于在 JWT（JSON Web Token）的签名和验证过程中使用 HMAC-SHA 签名算法
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 根据负载生成 jwt token
    public String createToken(Map<String, Object> claims) {

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
                .signWith(SignatureAlgorithm.HS256,getSecretKey())
                .compact();
    }

    public String getUserIdFromToken(String token) {
        Map<String,Object> claims = (Map<String, Object>) getJwsFromToken(token).getBody();
        return (String) claims.get(Constants.KEY_CLAIMS_PUT);
    }

    // 解析 Jws，从 token 中获取荷载
    private Jws getJwsFromToken(String token) {
        Jws<Claims> claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
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
}
