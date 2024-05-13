package org.example.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author TZH
 */
@Data
public class Feedback {
    private Integer id;
    /**
     * userId: 反馈用户Id
     * createTime: 反馈时间
     * category: 反馈类型
     * content: 反馈类型
     * state: 对用户反馈的响应状态,分为：未查看、已查看未处理、已处理
     */
    private Integer userId;
    private LocalDateTime createTime;
    private String category;
    private String content;
    private String state;
}
