package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.pojo.User;

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
    @Select("select * from tb_user where username = #{username}")
    User findByUserName(
            String username
    );

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     */
    @Insert("insert into tb_user(username,password,email,create_time) values(#{username},#{password},#{email},now())" )
    void add(
            String username,
            String password,
            String email
    );

    /**
     * 此次采用动态SQL
     * @param user 用户类对象
     */
    void update(User user);

    /**
     * 通过邮箱查找用户
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("select * from tb_user where email = #{email}")
    User findByUserEmail(String email);
}
