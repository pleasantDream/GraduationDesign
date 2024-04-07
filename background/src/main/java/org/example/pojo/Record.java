package org.example.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author TZH
 */
@Data
public class Record {
    private Integer id;
    private Integer userId;
    /**
     * 体检时间
     */
    private LocalDateTime time;
    /**
     * categoryId: 分类ID
     * name: 体检名
     * 1.体格测量
     * 2.血压测量
     * 3.体温测量
     * 4.血液检测
     * 5.尿液分析
     */
    private Integer categoryId;
    private String name;
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
     * 血压测量
     * highPressure: 高压
     * lowPressure: 低压
     */
    private float highPressure;
    private float lowPressure;

    /**
     * 体温测量
     * temperature: 体温(摄氏度)
     */
    private float temperature;

    /**
     * 血液检测
     * hb: 血红蛋白(克/升)
     * wbc: 白细胞计数( WBC * 10^9/升)
     * plt: 血小板计数( WBC * 10^9/升)
     * glucose: 血糖(毫克/分升)
     */
    private float hb;
    private float wbc;
    private float plt;
    private float glucose;

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
    private float protein;
    private String ket;
    private String bld;
    private float le;
    private float cc;
}
