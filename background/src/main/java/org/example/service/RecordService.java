package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.pojo.*;

import org.hibernate.validator.internal.util.StringHelper;
import org.json.JSONException;

import java.io.IOException;


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
    String recordPhysical(Physical physical) throws JSONException, IOException;

    /**
     * 血液分析记录
     * @param blood 血液类实例
     * @return 分析建议结果
     * @throws JSONException Json格式异常
     * @throws IOException I/O 异常
     */
    String recordBlood(Blood blood) throws JSONException, IOException;

    /**
     * 血压分析记录
     * @param pressure 血压类实例
     * @return 分析建议结果
     * @throws JSONException Json格式异常
     * @throws IOException I/O 异常
     */
    String recordPressure(Pressure pressure) throws JSONException, IOException;

    /**
     * 温度分析记录
     * @param temperature 温度类实例
     * @return 分析建议结果
     * @throws JSONException Json格式异常
     * @throws IOException I/O 异常
     */
    String recordTemperature(Temperature temperature) throws JSONException, IOException;

    /**
     * 尿液分析记录
     * @param urine 尿液类实例
     * @return 分析建议结果
     * @throws JSONException Json格式异常
     * @throws IOException I/O 异常
     */
    String recordUrine(Urine urine) throws JSONException, IOException;

    Physical recordPhysicalGet();
}
