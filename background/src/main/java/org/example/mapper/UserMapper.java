package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.pojo.User;

@Mapper
public interface UserMapper {
    // 根据用户名查询用户
    @Select("select * from tb_user where username = #{username}")
    User findByUserName(String username);

    // 注册用户
    @Insert("insert into tb_user(username,password,email,create_time) values(#{username},#{password},#{email},now())" )
    void add(String username, String password, String email);

    // 更新用户信息, 此处采用动态SQL
    void update(User user);

    /**
     * 根据邮箱查询用户
      */
    @Select("select * from tb_user where email = #{email}")
    User findByUserEmail(String email);
}
