package org.example.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author TZH
 */
@Data
public class Physical {
    private Integer id;
    private String gender;
    private Integer age;
    private Integer userId;
    private LocalDateTime time;
    /**
     * 体格测量
     * height: 身高(米)
     * weight: 体重(千克)
     * bmi: bmi = 体重（千克）/ 身高（米）的平方
     */
    private float height;
    private float weight;
    private float bmi;
    /**
     *  本次体检分析和建议结果
     */
    private String result;
}
