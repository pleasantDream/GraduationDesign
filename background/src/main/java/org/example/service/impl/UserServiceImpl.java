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
     *  @Autowired是Spring框架特有的注解，默认按照类型(byType)进行自动装配。
     *  如果找到多个相同类型的 Bean(一个接口可能有多个实现类，但大多数情况下约定只有一个)，
     *  则可以通过@Qualifier 注解来指定具体要注入的 Bean
     */
    @Autowired
    private UserMapper userMapper;

    /**
     *  Spring 框架提供了对@Resource注解的支持，所以可以注入到ioc容器里
     *  @Resource 是java EE 标准提供的规范
     *  @Resource 按名称自动匹配，若没有查找到匹配的名称按照类型进行注入
     */
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public User findByUserName(String username){
        User user = userMapper.findByUserName(username);

        return user;
    }

    @Override
    public Result<User> register(String username, String password, String rePassword, String email, String code, String reCode) {
        // 确认
        if(!password.equals(rePassword)){
            return Result.error("两次密码输入不一致");
        }
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
        if(!code.equals(reCode)){
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
    public void updateInfo(User user) {
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
            htmlEmail.setSubject("智慧健康: 你的邮箱验证码");
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

            return "发送失败";
        }
    }

    @Override
    public Result updateEmail(Map<String, String> params) {
        String email = params.get("email");
        String code = params.get("code");
        String recode = params.get("recode");
        if(!code.equals(recode)){
            return Result.error("验证码错误");
        }

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer)map.get("id");
        userMapper.updateEmail(email,id);

        return Result.success();
    }

    @Override
    public Result updatePassword(Map<String, String> params) {
        String oldPwd =  params.get("oldPwd");
        String newPwd =  params.get("newPwd");
        String rePwd =  params.get("rePwd");

        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userMapper.findByUserName(username);

        if(!Md5Util.getMD5String(oldPwd).equals(user.getPassword())){
            return Result.error("原密码错误");
        }
        if(!newPwd.equals(rePwd)){
            return Result.error("两次密码不一致");
        }
        String md5String = Md5Util.getMD5String(newPwd);
        Integer id = (Integer)map.get("id");
        userMapper.updatePwd(md5String,id);

        return Result.success();
    }
}
