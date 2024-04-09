package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
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

    @Insert("insert into tb_blood (gender,age,userId,time,hb,wbc,plt,glucose,result) values(#{gender}," +
            "#{age},#{userId},now(),#{hb},#{wbc},#{plt},#{glucose},#{result})")
    void addBlood(Blood blood);

    @Insert("insert into tb_pressure (gender,age,userId,time,highPressure,lowPressure,result) values(#{gender}," +
            "#{age},#{userId},now(),#{highPressure},#{lowPressure},#{result})")
    void addPressure(Pressure pressure);

    @Insert("insert into tb_temperature (gender,age,userId,time,temperature,result) values(#{gender}," +
            "#{age},#{userId},now(),#{temperature},#{result})")
    void addTemperature(Temperature temperature);

    @Insert("insert into tb_urine (gender,age,userId,time,sg,ph,glucose,protein,ket,bld,le,cc,result) values(#{gender}," +
            "#{age},#{userId},now(),#{sg},#{ph},#{glucose},#{protein},#{ket},#{bld},#{le},#{cc},#{result})")
    void addUrine(Urine urine);
}
