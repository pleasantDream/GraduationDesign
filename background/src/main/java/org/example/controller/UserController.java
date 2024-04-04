package org.example.controller;

import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import org.apache.ibatis.annotations.Param;
import org.example.pojo.Result;
import org.example.pojo.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.DrbgParameters;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    // 注册接口
    @PostMapping("/register")
    public Result register(@Param("username") String username,@Param("password") String password){
        // 得到响应结果
        Result result = userService.register(username, password);
        return result;
    }

    // 登录接口
    @PostMapping("/login")
    public Result login(@Param("username") String username,@Param("password") String password){
        Result result = userService.login(username, password);
        return  result;
    }

    // 获取用户信息
    @GetMapping("/userInfo")
    public Result<User> userInfo(){
        User user = userService.userInfo();
        return Result.success(user);
    }

    // 更新用户基本信息
    @PutMapping("/update")
    public Result update(@RequestBody User user){
        // @RequestBody User 注解将 json 数据转换成User 类型数据
        userService.update(user);
        return Result.success();
    }
}
