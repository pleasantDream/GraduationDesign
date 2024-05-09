package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.pojo.*;

import java.util.List;


/**
 * @author TZH
 */
@Mapper
public interface RecordMapper {

    /**
     * 体格检查
     * @param physical 体格类实例
     */
    @Insert("insert into tb_physical (gender,age,userId,time,height,weight,bmi,result) values(#{gender}," +
            "#{age},#{userId},NOW(),#{height},#{weight},#{bmi},#{result})")
    void addPhysical(Physical physical);

    /**
     * 血液分析
     * @param blood 血液类实例
     */
    @Insert("insert into tb_blood (gender,age,userId,time,hb,wbc,plt,glucose,result) values(#{gender}," +
            "#{age},#{userId},NOW(),#{hb},#{wbc},#{plt},#{glucose},#{result})")
    void addBlood(Blood blood);

    /**
     * 血压测量
     * @param pressure 血压类实例
     */
    @Insert("insert into tb_pressure (gender,age,userId,time,highPressure,lowPressure,result) values(#{gender}," +
            "#{age},#{userId},NOW(),#{highPressure},#{lowPressure},#{result})")
    void addPressure(Pressure pressure);

    /**
     * 温度分析
     * @param temperature 温度类实例
     */
    @Insert("insert into tb_temperature (gender,age,userId,time,temperature,result) values(#{gender}," +
            "#{age},#{userId},NOW(),#{temperature},#{result})")
    void addTemperature(Temperature temperature);

    /**
     * 尿液分析
     * @param urine 尿液类实例
     */
    @Insert("insert into tb_urine (gender,age,userId,time,sg,ph,protein,le,result) values(#{gender}," +
            "#{age},#{userId},NOW(),#{sg},#{ph},#{protein},#{le},#{result})")
    void addUrine(Urine urine);


    @Select("select * from tb_physical where userid = #{userId} order by time DESC LIMIT 1")
    Physical getPhysical(Integer userId);

    @Select("select * from tb_pressure where userid = #{userId}")
    Pressure getPressure(Integer userId);

    @Select("select * from tb_blood where userid = #{userId}")
    Blood getBlood(Integer userId);

    @Select("select * from tb_temperature where userid = #{userId}")
    Temperature getTemperature(Integer userId);

    @Select("select * from tb_urine where userid = #{userId}")
    Urine getUrine(Integer userId);

    @Update("insert into tb_physical (gender, age, userId, time, height, weight, bmi, result) " +
            "values (#{gender}, #{age}, #{userId}, NOW(), #{height}, #{weight}, #{bmi}, #{result})")
    void physicalUpdate(Physical physical);


    @Update("update tb_blood set gender=#{gender}, age=#{age}, time=NOW(), hb=#{hb}, wbc=#{wbc}, " +
            "plt=#{plt}, glucose=#{glucose}, result=#{result} where userid=#{userId}")
    void bloodUpdate(Blood blood);

    @Update("update tb_pressure set gender=#{gender}, age=#{age}, time=NOW(), highpressure=#{highPressure}," +
            "lowpressure=#{lowPressure}, result=#{result} where userid=#{userId}")
    void pressureUpdate(Pressure pressure1);

    @Update("update tb_temperature set gender=#{gender}, age=#{age}, time=NOW(), temperature=#{temperature}," +
            "result=#{result} where userid=#{userId}")
    void temperatureUpdate(Temperature temperature1);

    @Update("update tb_urine set gender=#{gender}, age=#{age}, time=NOW(), sg=#{sg}," +
            "ph=#{ph}, protein=#{protein}, le=#{le}, result=#{result} where userid=#{userId}")
    void urineUpdate(Urine urine1);

    @Insert("insert into tb_history (user_id,create_time,question,answer,item) values(#{userId},NOW()," +
            "#{question},#{answer},#{item})")
    void addHsitory(Integer userId,String question, String answer, String item);

    @Select("select * from tb_history where user_id=#{userId} and item=#{item}" +
            "order by create_time desc limit #{startRow},5")
    List<History> getHistory(Integer userId,String item,Integer startRow);

    @Select("select count(*) from tb_history where user_id=#{userId} and item=#{item}")
    Integer getSum(Integer userId, String item);

    @Delete("delete from tb_history where user_id=#{userId} and item=#{item}")
    void deleteHistory(Integer userId, String item);

    @Select({
            "SELECT COUNT(*) FROM",
            "${item}",
            "WHERE userId = #{userId}"
    })
    Integer getCount(Integer userId, String item);

    @Delete({
            "DELETE FROM",
            "${item}",
            "WHERE userId = #{userId} ORDER BY time LIMIT 1"
    })
    void deleteCount(Integer userId, String item);
}
