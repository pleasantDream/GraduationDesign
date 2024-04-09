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
     * glucose: 血糖(mg/dl) ,和血液检测共有一个字段
     * protein: 蛋白质(mg/dl)
     * ket: 酮体,以阴性或阳性表示
     * bld: 潜血,以阴性或阳性表示
     * le: 白细胞酯酶(U/l)
     * cc: 细胞计数(cells/mL)
     */
    private float sg;
    private float ph;
    private float glucose;
    private float protein;
    private String ket;
    private String bld;
    private float le;
    private float cc;
    /**
     *  本次体检分析和建议结果
     */
    private String result;
}
