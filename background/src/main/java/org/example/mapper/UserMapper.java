package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.User;

@Mapper
public interface UserMapper {
    // 根据用户名查询用户
    @Select("select * from tb_user where username = #{username}")
    User findByUserName(String username);

    @Insert("insert into tb_user(username,password,create_time) values(#{username},#{password},now())" )
    void add(String username, String password);
}
