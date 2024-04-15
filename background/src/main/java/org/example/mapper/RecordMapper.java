package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.*;


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
            "#{age},#{userId},now(),#{height},#{weight},#{bmi},#{result})")
    void addPhysical(Physical physical);

    /**
     * 血液分析
     * @param blood 血液类实例
     */
    @Insert("insert into tb_blood (gender,age,userId,time,hb,wbc,plt,glucose,result) values(#{gender}," +
            "#{age},#{userId},now(),#{hb},#{wbc},#{plt},#{glucose},#{result})")
    void addBlood(Blood blood);

    /**
     * 血压测量
     * @param pressure 血压类实例
     */
    @Insert("insert into tb_pressure (gender,age,userId,time,highPressure,lowPressure,result) values(#{gender}," +
            "#{age},#{userId},now(),#{highPressure},#{lowPressure},#{result})")
    void addPressure(Pressure pressure);

    /**
     * 温度分析
     * @param temperature 温度类实例
     */
    @Insert("insert into tb_temperature (gender,age,userId,time,temperature,result) values(#{gender}," +
            "#{age},#{userId},now(),#{temperature},#{result})")
    void addTemperature(Temperature temperature);

    /**
     * 尿液分析
     * @param urine 尿液类实例
     */
    @Insert("insert into tb_urine (gender,age,userId,time,sg,ph,glucose,protein,ket,bld,le,cc,result) values(#{gender}," +
            "#{age},#{userId},now(),#{sg},#{ph},#{glucose},#{protein},#{ket},#{bld},#{le},#{cc},#{result})")
    void addUrine(Urine urine);


    @Select("select * from tb_physical where userid = #{userId}")
    Physical getPhysical(Integer userId);
}
