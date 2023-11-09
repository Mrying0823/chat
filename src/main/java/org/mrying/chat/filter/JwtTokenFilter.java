package org.mrying.chat.filter;

import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.NotNull;
import org.mrying.chat.constants.RedisKey;
import org.mrying.chat.utils.JwtUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 邓和颖
 * @since 2023/11/8 9:36
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private JwtUtils jwtUtils;

    // 检查用户提供的令牌，验证用户的身份，并将用户的身份信息存储在 Spring Security 上下文中
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        // 获取 token
        String token = request.getHeader("Authorization");
        if (!StrUtil.isNotBlank(token)) {

            // 没有 token 直接放行
            filterChain.doFilter(request, response);
            return;
        }

        // 有 token，获取 userPhone
        String userId;
        try {
            userId = jwtUtils.getUserIdFromToken(token);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token illegal");
        }

        // 检查是否与 redis 中的 token 相同（是否退出登录）
        if (!token.equals(stringRedisTemplate.opsForValue().get(RedisKey.KEY_USER_TOKEN+userId))) {
            throw new RuntimeException("token illegal");
        }

        // 将 userPhone 信息存入 SecurityContextHolder
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userId, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        // 放行
        filterChain.doFilter(request, response);
    }
}
