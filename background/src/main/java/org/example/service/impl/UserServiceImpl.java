package org.example.service.impl;

import jakarta.annotation.Resource;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.example.mapper.UserMapper;
import org.example.pojo.Result;
import org.example.pojo.User;
import org.example.service.UserService;
import org.example.utils.JwtUtil;
import org.example.utils.Md5Util;
import org.example.utils.ThreadLocalUtil;
import org.example.utils.ValidateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author TZH
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     *  @Autowired 和 @Resource 都是 Java 中用于注入依赖的注解，但它们有一些不同之处。
     *  @Autowired 注解默认按照 bean 的类型进行自动装配，通过 @Qualifier 注解指定要注入的 bean 的名称。
     *  @Autowired 注解主要用于按类型自动装配，并且支持通过 @Qualifier 注解指定名称。
     */
    @Autowired
    private UserMapper userMapper;

    /**
     * StringRedisTemplate 是 Spring Framework 提供的一个用于操作 Redis 数据库的模板类，
     * 主要用于操作 Redis 中的字符串类型数据。它是基于 RedisTemplate 的一个简化版本，专门用于处理字符串类型的数据。
     * 用 @Resource 注解来标识需要注入的依赖资源
     */
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public User findByUserName(String username){
        User user = userMapper.findByUserName(username);
        return user;
    }

    @Override
    public Result<User> register(String username, String password, String email, String code, String recode) {
        // 查询用户
        User user = findByUserName(username);
        if(user != null){
            return Result.error("用户名已被使用");
        }
        user = userMapper.findByUserEmail(email);
        if(user != null){
            return Result.error("该邮箱已被使用");
        }
        // 验证码验证
        if(!code.equals(recode)){
            return Result.error("验证码错误");
        }
        // 加密
        String md5String = Md5Util.getMD5String(password);
        // 添加
        userMapper.add(username, md5String, email);
        return Result.success();
    }

    @Override
    public Result login(String username, String password) {
        User user = findByUserName(username);
        if(user == null){
            return Result.error("该用户名未注册");
        }
        if (!Md5Util.getMD5String(password).equals(user.getPassword())){
            return Result.error("密码错误");
        }
        // 登录成功,claims指jwt令牌中有效载荷的json数据，因为可能有多个载荷，所以用map存储
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id",user.getId());
        claims.put("username",user.getUsername());
        String token = JwtUtil.genToken(claims);
        // 把token存储到Redis中，实现jwt令牌的主动失效
        // opsForValue()：获取用于操作字符串类型数据的操作对象。
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        // 键值都存的一样
        // 和jwt设定的一天时间匹配
        operations.set(token, token, 24, TimeUnit.HOURS);
        return Result.success(token);
    }

    @Override
    public User userInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userMapper.findByUserName(username);
        return user;
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public String emailValidation(String email) {
        try {
            //创建一个Html实例对象
            HtmlEmail htmlEmail = new HtmlEmail();
            //邮箱的smtp服务器，一般中间的哪个就是你用的邮箱的名字
            htmlEmail.setHostName("smtp.qq.com");
            //设置发送的字符编码
            htmlEmail.setCharset("utf-8");
            //设置收件人
            htmlEmail.addTo(email);
            //设置发送人的邮箱和用户名
            htmlEmail.setFrom("3385369312@qq.com","智慧健康");
            //设置发送人的用户名和授权码
            htmlEmail.setAuthentication("3385369312@qq.com","rqnlgkloljufciji");
            //设置发送标题
            htmlEmail.setSubject("你的邮箱验证码");
            //设置发送内容
            Integer message = ValidateCodeUtil.generateValidateCode(6);
            String code = String.valueOf(message);
            htmlEmail.setMsg(code);
            //发送
            htmlEmail.send();
            return code;
        }
        catch (EmailException e) {
            e.printStackTrace();
            return "";
        }
    }
}
