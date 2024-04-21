package org.example.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author TZH
 */
@Data
public class History {
    private Integer id;
    private Integer userId;
    private Date createTime;
    private String question;
    private String answer;
}
