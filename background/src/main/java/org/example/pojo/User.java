package org.example.pojo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.jacoco.agent.rt.internal_43f5073.Agent;

import java.time.LocalDateTime;

// lombok 在编译阶段，为实体类自动生成setter   getter  toString等方法
// 需要在pom文件中引入该依赖    在实体类上添加注解  @Data
@Data
//用户类：存储用户的基本信息，如用户名、密码、电子邮箱等。
public class User {
    private Integer id;
    private String username;  // 用户名，就是用户账号
    private String password;
    private String nickname;//昵称
    @NotEmpty
    @Email
    private String email;
    private String name; //姓名
    private String gender;
    private String phoneNumber;
    private String userPic;//用户头像地址
    private LocalDateTime createTime;//创建时间
    private Integer age;
}
