package org.mrying.chat.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 邓和颖
 * @since 2023/11/9 16:22
 */
public class SecurityContextHolderUtils {
    // 获取 SecurityContextHolder 中的 id
    public static String getUserId() {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            return (String) authenticationToken.getPrincipal();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
