package org.example.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import org.apache.ibatis.annotations.Param;
import org.example.pojo.Result;
import org.example.pojo.User;
import org.example.service.UserService;
import org.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.DrbgParameters;
import java.util.Map;

/**
 * @author TZH
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * 注册
     * @param username  用户名
     * @param password 密码
     * @param email 邮箱
     * @param code 用户输入的验证码
     * @param reCode 前端收到的验证码
     * @return 注册是否成功
     */
    @PostMapping("/register")
    public Result register(
        @Param("username") String username,
        @Param("password") String password,
        @Param("password") String rePassword,
        @Param("email") @Email String email,
        @Param("code") String code,
        @Param("reCode") String reCode
    ){
        // 得到响应结果
        Result result = userService.register(username, password, rePassword,email, code, reCode);

        return result;
    }

    /**
     *
     * @param email 用户邮箱
     * @return 发送验证码给邮箱同时将其返回给前端
     */
    @GetMapping("/register")
    public String emailValidation(@Param("email") @Email String email){
        System.out.println("验证码发送接口被调用");
        String ValidateCode = userService.emailValidation(email);

        // 返回校验码给前端
        return ValidateCode;
    }


    /**
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录是否成功
     */
    @PostMapping("/login")
    public Result login(@Param("username") String username,@Param("password") String password){
        System.out.println("登录接口被调用");
        Result result = userService.login(username, password);

        return  result;
    }

    /**
     * 获取用户信息
     * @return 返回用户信息
     */
    @GetMapping("/userInfo")
    public Result<User> userInfo(){
        System.out.println("获取用户信息接口被调用");
        User user = userService.userInfo();

        return Result.success(user);
    }

    /**
     * 更新用户基本信息
     * @param user 用户实体类的对象
     * @return 更新用户基本信息是否成功
     */
    @PutMapping("/info/update")
    public Result updateInfo(@RequestBody User user){
        System.out.println("更新用户信息接口被调用");
        // @RequestBody User 注解将 json 数据转换成User 类型数据
        userService.updateInfo(user);

        return Result.success();
    }

    @PatchMapping("/email/update")
    public Result updateEmail(@RequestBody Map<String,String> params){
        System.out.println("重置邮箱接口被调用");
        Result result = userService.updateEmail(params);

        return result;
    }

    @PatchMapping("/password/update")
    public Result updatePassword(@RequestBody Map<String,String> params){
        System.out.println("重置密码接口被调用");
        Result result = userService.updatePassword(params);

        return result;
    }
}
