package org.mrying.chat.service;

import org.mrying.chat.model.User;

import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/11/7 10:27
 */
public interface UserService {

    // 查询用户表中所有的手机号
    List<String> queryUserPhone();

    // 注册用户
    int userRegister(String phone,String passwd, String userName);

    // 用户登录
    User queryUserByPhoneAndPwd(String phone, String passwd);
}
