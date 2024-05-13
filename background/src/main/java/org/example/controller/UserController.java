package org.example.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import org.apache.ibatis.annotations.Param;
import org.example.pojo.Feedback;
import org.example.pojo.Result;
import org.example.pojo.User;
import org.example.service.UserService;
import org.example.utils.ThreadLocalUtil;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.DrbgParameters;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
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
     * @param email 用户最后输入的邮箱
     * @param reEmail 发送验证码用的邮箱
     * @param code 用户输入的验证码
     * @param reCode 前端收到的验证码
     * @return 注册是否成功
     * 防止用户发送验证码后修改邮箱
     */
    @PostMapping("/register")
    public Result register(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        @RequestParam("password") String rePassword,
        @RequestParam("email") @Email String email,
        @RequestParam("reEmail") @Email String reEmail,
        @RequestParam("code") String code,
        @RequestParam("reCode") String reCode
    ){
        // 返回响应结果
        return userService.register(username, password, rePassword,email,reEmail, code, reCode);
    }

    /**
     *
     * @param email 用户邮箱
     * @return 发送验证码给邮箱同时将其返回给前端
     */
    @GetMapping("/emailValidation")
    public String emailValidation(@RequestParam("email") @Email String email){
        System.out.println("验证码发送接口");
        String code = userService.emailValidation(email);
        System.out.println(code);
        // 返回验证码和对应的邮箱给前端
        return code;
    }

    @PostMapping("/forgetPassword")
    public Result forgetPassword(@RequestParam("newPassword") String newPassword,
                                 @RequestParam("rePassword") String rePassword,
                                 @RequestParam("email") @Email String email,
                                 @RequestParam("code") String code,
                                 @RequestParam("reCode") String reCode){
        System.out.println("忘记密码");
        return userService.forgetPassword(newPassword, rePassword, email, code, reCode);
    }

    /**
     *
     * @param email
     * @param reEmail
     * @param code
     * @param reCode
     * @return
     */
    @PostMapping("/loginByEmail")
    public Result loginByEmail(@RequestParam("email") @Email String email,
                               @RequestParam("reEmail") @Email String reEmail,
                               @RequestParam("code") String code,
                               @RequestParam("reCode") String reCode){
        System.out.println("通过邮箱登录");
        // 返回响应结果
        return userService.loginByEmail(email,reEmail,code,reCode);

    }

    /**
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录是否成功
     */
    @PostMapping("/login")
    public Result login(@RequestParam("username") String username,
                        @RequestParam("password") String password){
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

    @PatchMapping("/avatar/updata")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PostMapping("/feedback/add")
    public Result feedbackAdd(@RequestBody Map<String,String> params){
        System.out.println("提交反馈意见");
        userService.feedbackAdd(params);
        return Result.success();
    }

    @GetMapping("/feedback/get")
    public List<Feedback> feedbackGet(@RequestParam("category") String category){
        System.out.println("获取反馈意见");
        return userService.feedbackGet(category);
    }
}
