package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.pojo.Feedback;
import org.example.pojo.User;

import java.util.List;

/**
 * @author TZH
 */
@Mapper
public interface UserMapper {
    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return 用户信息
     */
    @Select("select * from tb_user where binary username = #{username}")
    User findByUserName(String username);

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     */
    @Insert("insert into tb_user(username,password,email,create_time) values(#{username},#{password},#{email},NOW())" )
    void add(
            String username,
            String password,
            String email
    );

    /**
     * 这里采用动态SQL
     * @param user 用户类对象
     */
    void update(User user);

    @Update("update tb_user set email=#{email} where id =#{id}")
    void updateEmail(String email, Integer id);

    /**
     * 通过邮箱查找用户
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("select * from tb_user where email = #{email}")
    User findByUserEmail(String email);


    @Update("update tb_user set password=#{password} where id=#{id}")
    void updatePwd(String password, Integer id);

    @Update("update tb_user set user_pic=#{avatarUrl} where id=#{id}")
    void updateAvatar(String avatarUrl, Integer id);

    @Update("update tb_user set password=#{md5String} where email=#{email}")
    void updatePwdByEmail(String md5String, String email);

    @Insert("insert into tb_feedback(userId, createTime, category, content) " +
            "values(#{userId}, now(), #{category}, #{content})")
    void feedbackAdd(Integer userId, String category, String content);

    @Select("select * from tb_feedback where userId=#{userId} and category=#{category} ORDER BY createTime DESC")
    List<Feedback> feedbackGet(Integer userId, String category);
}
