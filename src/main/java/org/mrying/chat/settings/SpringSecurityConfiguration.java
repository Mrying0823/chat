package org.mrying.chat.settings;

import org.mrying.chat.filter.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author 邓和颖
 * @since 2023/11/7 15:59
 */
@EnableWebSecurity
@Configuration
public class SpringSecurityConfiguration {

    @Resource
    JwtTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 禁止跨域防护
        http.csrf().disable().cors();

        // 基于 token，将会话创建策略设置为无状态
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests()
                .mvcMatchers("/doc.html/**",
                        "/swagger-ui.html/**",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/v2/api-docs-ext").permitAll()
                .mvcMatchers("/v1/user/phoneExists","/v1/user/register","/v1/user/login").permitAll()
                .mvcMatchers("/v1/questionList/publicQuestionList").permitAll()
                .anyRequest()
                .authenticated();

        // 在中间插入一个Filter
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
