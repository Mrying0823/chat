package org.mrying.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.mrying.chat.model.User;

/**
 * @author 邓和颖
 */
@Mapper
public interface UserMapper {

    // 查询用户
    User selectUserById(String userId);

    // 扣除公用 api 使用次数
    int updateUserTokenCount(User user);
}