package org.mrying.chat.utils;

import java.util.UUID;

/**
 * @author 邓和颖
 */

public class UUIDUtils {

    // 随机生成一个 ID

    /**
     * 返回 UUID 的值
     * @return UUID
     */
    public static String getUUID() {
        // 得到一个 32 字节的字符串，将横杠进行替换
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
