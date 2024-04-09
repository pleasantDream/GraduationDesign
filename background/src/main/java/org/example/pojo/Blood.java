package org.example.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author TZH
 */
@Data
public class Blood {
    private Integer id;
    private String gender;
    private Integer age;
    private Integer userId;
    private LocalDateTime time;
    /**
     * 血液检测
     * hb: 血红蛋白(克/升)
     * wbc: 白细胞计数( WBC * 10^9/升)
     * plt: 血小板计数( WBC * 10^9/升)
     * glucose: 血糖(毫克/分升)(空腹状态)
     */
    private float hb;
    private float wbc;
    private float plt;
    private float glucose;
    /**
     *  本次体检分析和建议结果
     */
    private String result;
}
