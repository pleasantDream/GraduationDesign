package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author TZH
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean<T>{
    /**
     * total: 总条数
     * items: 当前页数据集合
     */
    private Long total;
    private List<T> items;
}
