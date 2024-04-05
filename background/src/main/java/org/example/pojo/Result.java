package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装所有实体类
 * @author TZH
 * @param <T>
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    /**
     * 业务状态码  0-成功  1-失败
     */
    private Integer code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    /**
     * 快速返回操作成功响应结果(带响应数据)
     * @param data 传入数据
     * @param <E> 泛型
     * @return 对象
     */
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "操作成功", data);
    }

    /**
     * 快速返回操作成功响应结果
     * @return 成功信息
     */
    public static Result success() {
        return new Result(0, "操作成功", null);
    }

    public static Result error(String message) {

        return new Result(1, message, null);
    }
}