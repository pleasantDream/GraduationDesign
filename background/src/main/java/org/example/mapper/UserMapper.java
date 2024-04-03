package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.User;

@Mapper
public interface UserMapper {
    // 根据用户名查询用户
    @Select("select * from user where username = #{username}")
    User findByUserName(String username);

    @Insert("insert into user(username,password,create_time) value(#{username},#{password),now()" )
    void add(String username, String md5String);
}
