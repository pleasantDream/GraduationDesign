package org.example.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author TZH
 */
@Data
public class Temperature {
    private Integer id;
    private String gender;
    private Integer age;
    private Integer userId;
    private LocalDateTime time;
    /**
     * 体温测量
     * temperature: 体温(摄氏度)
     */
    private float temperature;
    /**
     *  本次体检分析和建议结果
     */
    private String result;
}
