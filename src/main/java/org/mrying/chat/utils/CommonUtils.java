package org.mrying.chat.utils;

import java.util.regex.Pattern;

/**
 * @author 邓和颖
 * @since 2023/9/24 19:58
 */
public class CommonUtils {

    // 处理 pageNo
    public static int defaultPageNo(Integer pageNo) {
        return (pageNo == null || pageNo < 1) ? 1 : pageNo;
    }

    // 处理 pageSize
    public static int defaultPageSize(Integer pageSize) {
        return (pageSize == null || pageSize < 1) ? 1 : pageSize;
    }

    // 手机号敏感处理
    public static String maskPhone(String phone) {
        return (phone == null || phone.isEmpty() || !Pattern.matches("^1[3-9]\\d{9}$",phone))
                ? "***********" : phone.substring(0,3)+"******"+phone.substring(9,11);
    }

    // 手机号格式检验
    public static boolean checkPhone(String phone) {
        return Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", phone);
    }
}
