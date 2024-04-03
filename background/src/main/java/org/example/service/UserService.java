package org.example.service;

import org.example.pojo.Result;
import org.example.pojo.User;

public interface UserService {
    // 根据用户名查询用户
    User findByUserName(String username);

    // 注册用户
    Result<User> register(String username, String password);
}
