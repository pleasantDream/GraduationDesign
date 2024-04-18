package org.example.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author TZH
 */
@Data
public class Urine {
    private Integer id;
    private String gender;
    private Integer age;
    private Integer userId;
    private LocalDateTime time;
    /**
     * 尿液分析
     * sg: 尿液比重，无单位，通常在1.005-1.030之间
     * ph: 尿液PH值，无单位,通常在4.6-8.0之间
     * protein: 蛋白质(mg/dl)
     * le: 白细胞酯酶(U/l)
     */
    private float sg;
    private float ph;
    private float protein;
    private float le;
    /**
     *  本次体检分析和建议结果
     */
    private String result;
}
