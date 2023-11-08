package org.mrying.chat.service.impl;

import org.mrying.chat.constants.RedisKey;
import org.mrying.chat.mapper.UserMapper;
import org.mrying.chat.model.User;
import org.mrying.chat.service.UserService;
import org.mrying.chat.utils.CommonUtils;
import org.mrying.chat.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 邓和颖
 * @since 2023/11/7 10:40
 */
@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    // 设置密码加密器
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 初始化 AuthenticationManager
    private final AuthenticationManager authenticationManager = authenticationManager();

    // 配置 AuthenticationManager，设置 UserDetailsService
    private AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        // 添加 passwordEncoder
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        // 添加 UserDetailsService
        daoAuthenticationProvider.setUserDetailsService(this);

        return new ProviderManager(daoAuthenticationProvider);
    }

    private StringRedisTemplate stringRedisTemplate;

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // UserDetailsService 的实现方法
    // loadUserByUsername 方法负责从数据库中查找用户信息，并将其封装为 Spring Security 的 UserDetails 对象
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        if(!CommonUtils.checkPhone(phone)) {
            throw new UsernameNotFoundException("手机号码格式不正确");
        }

        // 根据 phone 查询用户信息
        User user = userMapper.selectUserByPhoneAndPwd(phone,null);

        if(user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return new org.mrying.chat.pojo.UserDetails(user);
    }

    // 查询所有用户手机号
    @Override
    public List<String> queryUserPhone() {
        return userMapper.selectUserPhone();
    }

    // 注册新用户
    @Override
    public int userRegister(String phone, String passwd, String userName) {
        // 默认操作失败
        int result = 0;

        // 需要在配置文件写入关于 redis 的配置信息
        SetOperations<String,String> set = stringRedisTemplate.opsForSet();

        // 检查参数
        if(CommonUtils.checkPhone(phone)
                && passwd != null && passwd.length() == 32
                // 验证手机是否已注册
                && Boolean.FALSE.equals(set.isMember(RedisKey.KEY_USER_PHONE, phone))) {

            // 注册新用户
            User user = new User();
            user.setUserId(UUIDUtils.getUUID());
            user.setUserPhone(phone);
            user.setUserPassword(passwordEncoder.encode(passwd));
            user.setUserName(userName);
            user.setUserTokenCount(100);

            userMapper.insertNewUser(user);

            // 操作成功
            result = 1;

            // 将当前注册的手机号加入 redis
            set.add(RedisKey.KEY_USER_PHONE, phone);
        }

        return result;
    }

    // 获取 redis 用户电话号码集合，这个不符合我的要求
    public Set<String> getUserPhonesFromRedis() {
        SetOperations<String, String> set = stringRedisTemplate.opsForSet();

        // 尝试从 Redis 获取用户电话号码集合
        Set<String> userPhones = set.members(RedisKey.KEY_USER_PHONE);

        if (userPhones == null || userPhones.isEmpty()) {
            // 查询用户表中所有的电话号码
            List<String> phoneList = userMapper.selectUserPhone();

            // 将电话号码添加到 Redis Set 中
            set.add(RedisKey.KEY_USER_PHONE, phoneList.toArray(new String[0]));

            // 设置过期时间
            stringRedisTemplate.expire(RedisKey.KEY_USER_PHONE, 1, TimeUnit.HOURS);

            return new HashSet<>(phoneList);
        }

        return userPhones;
    }

    // 用户登录
    @Override
    public User queryUserByPhoneAndPwd(String phone, String passwd) {
        User user = null;

        Set<String> userPhones = getUserPhonesFromRedis();

        // 检查参数
        if(CommonUtils.checkPhone(phone)
                && passwd != null && passwd.length() == 32
                // 验证手机是否已注册
                && userPhones.contains(phone)
        ) {

            // 构造 UsernamePasswordAuthenticationToken 类对象，作为 authenticate 的参数
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phone,passwd);

            // 调用 authentication，用于用户名和密码验证的令牌
            // Spring Security 的 DaoAuthenticationProvider 会自动调用 UserDetailsService 中的 loadUserByUsername 方法来查找用户信息，包括密码验证
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            if(authentication != null) {
                user = userMapper.selectUserByPhoneAndPwd(phone,null);
            }
        }
        return user;
    }
}




















































