package org.example.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author TZH
 */
@Data
public class Pressure {
    private Integer id;
    private String gender;
    private Integer age;
    private Integer userId;
    private LocalDateTime time;
    /**
     * 血压测量
     * highPressure: 高压(mmHg)
     * lowPressure: 低压(mmHg)
     */
    private float highPressure;
    private float lowPressure;
    /**
     *  本次体检分析和建议结果
     */
    private String result;
}
