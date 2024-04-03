package org.example.service.impl;

import org.example.mapper.UserMapper;
import org.example.pojo.Result;
import org.example.pojo.User;
import org.example.service.UserService;
import org.example.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PublicKey;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserName(String username){
        User user = userMapper.findByUserName(username);
        return user;
    }

    @Override
    public Result<User> register(String username, String password) {
        System.out.println(username);
        System.out.println(password);
        // 查询用户
        User user = findByUserName(username);
        if(user != null){
            return Result.error("用户名已被使用");
        }
        // 没有占用，注册
        // 加密
        String md5String = Md5Util.getMD5String(password);
        // 添加
        userMapper.add(username, md5String);
        return Result.success();
    }
}
