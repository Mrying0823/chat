package org.mrying.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mrying.chat.model.User;

import java.util.List;

/**
 * @author 邓和颖
 */
@Mapper
public interface UserMapper {

    // 查询用户
    User selectUserById(String userId);

    // 扣除公用 api 使用次数
    int updateUserTokenCount(User user);

    // 查询用户表中所有的手机号
    List<String> selectUserPhone();

    // 注册新用户
    int insertNewUser(User user);

    // 用户登录
    User selectUserByPhoneAndPwd(@Param("phone") String phone, @Param("loginPassword") String passwd);
}