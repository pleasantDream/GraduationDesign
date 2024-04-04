package org.example.service;

import org.example.pojo.Result;
import org.example.pojo.User;

public interface UserService {
    // 根据用户名查询用户
    User findByUserName(String username);

    // 注册
    Result<User> register(String username, String password);

    // 登录
    Result login(String username, String password);

    // 获取用户信息
    User userInfo();

    // 更新用户基本信息
    void update(User user);
}
