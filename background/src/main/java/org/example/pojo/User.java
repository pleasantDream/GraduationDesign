package org.example.pojo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.jacoco.agent.rt.internal_43f5073.Agent;

import java.time.LocalDateTime;

/**
 * 用户类：存储用户的基本信息，如用户名、密码、电子邮箱等。
 * lombok 在编译阶段，为实体类自动生成setter   getter  toString等方法
 * 需要在pom文件中引入该依赖    在实体类上添加注解  @Data
 * @author TZH
 */
@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String name;
    private String gender;
    private String userPic;
    private LocalDateTime createTime;
    private Integer age;
}
