package org.mrying.chat;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.mrying.chat.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 邓和颖
 */
@EnableSwagger2
@EnableSwaggerBootstrapUI
@SpringBootApplication
public class ChatApplication {

    @Value("${jwt.data.secret}")
    private String secret;

    @Value("${jwt.data.expiration}")
    private long expiration;

    // 创建 JwtUtils 对象
    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils(secret, expiration);
    }

    public static void main(String[] args) {
        try {
            SpringApplication.run(ChatApplication.class, args);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
