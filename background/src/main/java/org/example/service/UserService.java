package org.example.service;

import org.example.pojo.Result;
import org.example.pojo.User;

import java.util.Map;

/**
 * @author TZH
 */
public interface UserService {

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户类对象
     */
    User findByUserName(String username);

    /**
     * 注册
     * @param username  用户名
     * @param password 密码
     * @param rePassword 确认密码
     * @param email 邮箱
     * @param code 用户输入的验证码
     * @param reCode 前端收到的验证码
     * @return 注册是否成功
     */
    Result<User> register(String username, String password, String rePassword, String email, String reEmail,
                          String code, String reCode);

    /**
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录是否成功
     */
    Result login(String username, String password);

    /**
     * 获取用户信息
     * @return 返回用户信息
     */
    User userInfo();

    /**
     * 更新用户基本信息
     * @param user 用户实体类的对象
     * @return 更新用户基本信息是否成功
     */
    void updateInfo(User user);

    /**
     * 发送验证码给指定邮箱
     * @param email 邮箱
     * @return 验证码
     */
    String emailValidation(String email);

    /**
     *
     * @param params
     * @return
     */
    Result updateEmail(Map<String, String> params);


    Result updatePassword(Map<String, String> params);

    void updateAvatar(String avatarUrl);

    Result loginByEmail(String email, String reEmail,String code, String reCode);

    Result forgetPassword(String newPassword, String rePassword, String email, String code, String reCode);
}
