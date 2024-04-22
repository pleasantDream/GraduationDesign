package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.pojo.*;

import org.hibernate.validator.internal.util.StringHelper;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;


/**
 * @author TZH
 */
public interface RecordService {


    /**
     * 体格分析记录
     * @param physical 体格类实例
     * @return 分析建议结果
     * @throws JSONException Json格式异常
     * @throws IOException I/O 异常
     */
    Result recordPhysical(Physical physical) throws JSONException, IOException;

    /**
     * 血液分析记录
     * @param blood 血液类实例
     * @return 分析建议结果
     * @throws JSONException Json格式异常
     * @throws IOException I/O 异常
     */
    Result recordBlood(Blood blood) throws JSONException, IOException;

    /**
     * 血压分析记录
     * @param pressure 血压类实例
     * @return 分析建议结果
     * @throws JSONException Json格式异常
     * @throws IOException I/O 异常
     */
    Result recordPressure(Pressure pressure) throws JSONException, IOException;

    /**
     * 温度分析记录
     * @param temperature 温度类实例
     * @return 分析建议结果
     * @throws JSONException Json格式异常
     * @throws IOException I/O 异常
     */
    Result recordTemperature(Temperature temperature) throws JSONException, IOException;

    /**
     * 尿液分析记录
     * @param urine 尿液类实例
     * @return 分析建议结果
     * @throws JSONException Json格式异常
     * @throws IOException I/O 异常
     */
    Result recordUrine(Urine urine) throws JSONException, IOException;

    Physical recordPhysicalGet();

    Pressure recordPressureGet();

    Blood recordBloodGet();

    Temperature recordTemperatureGet();

    Urine recordUrineGet();

    Result recordPhysicalUpdate(Physical physical) throws JSONException, IOException;

    Result recordBloodUpdate(Blood blood) throws JSONException, IOException;

    Result recordPressureUpdate(Pressure pressure) throws JSONException, IOException;

    Result recordTemperatureUpdate(Temperature temperature) throws JSONException, IOException;

    Result recordUrineUpdate(Urine urine) throws JSONException, IOException;

    void test(String message) throws JSONException, IOException;

    List<History> recordHistory(Integer startRow, String item);

    String recordChat(String question, String item) throws JSONException, IOException;


}
