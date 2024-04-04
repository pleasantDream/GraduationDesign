package org.example.service.impl;

import jakarta.annotation.Resource;
import org.example.mapper.UserMapper;
import org.example.pojo.Result;
import org.example.pojo.User;
import org.example.service.UserService;
import org.example.utils.JwtUtil;
import org.example.utils.Md5Util;
import org.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    // @Autowired 注解默认按照 bean 的类型进行自动装配，通过 @Qualifier 注解指定要注入的 bean 的名称。
    // @Autowired 注解主要用于按类型自动装配，并且支持通过 @Qualifier 注解指定名称。

    @Autowired
    private UserMapper userMapper;

    //  StringRedisTemplate 是 Spring Framework 提供的一个用于操作 Redis 数据库的模板类，
    //  主要用于操作 Redis 中的字符串类型数据。它是基于 RedisTemplate 的一个简化版本，专门用于处理字符串类型的数据。
    //  用 @Resource 注解来标识需要注入的依赖资源
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public User findByUserName(String username){
        User user = userMapper.findByUserName(username);
        return user;
    }

    @Override
    public Result<User> register(String username, String password) {
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
        operations.set(token, token, 24, TimeUnit.HOURS); // 和jwt设定的一天时间匹配
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
}
