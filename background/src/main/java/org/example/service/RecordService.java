package org.example.service;

import org.springframework.stereotype.Service;

/**
 * @author TZH
 */
public interface RecordService {
    /**
     * 体格测量
     * @param height 身高
     * @param weight 体重
     * @param bmi   bmi指数
     * @return
     */
    String physicalMeasurement(float height, float weight, float bmi);
}
