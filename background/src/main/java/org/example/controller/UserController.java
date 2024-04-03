package org.example.controller;

import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import org.example.pojo.Result;
import org.example.pojo.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    // 注册接口
    @PostMapping("/register")
    public Result register(
            @Pattern(regexp = "^\\S{5,16}$") String username,
            @Pattern(regexp = "~\\S{5,16}$") String password

    ){
        // 得到响应结果
        Result result = userService.register(username, password);
        return result;
    }
}
