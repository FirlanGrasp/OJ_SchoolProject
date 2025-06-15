package com.yupi.yuoj.model.vo;

import lombok.Data;

import java.io.Serializable;

// 返回VO类
@Data
public class ClassSearchUserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 学生ID
     */
    private Long id;

    /**
     * 学号
     */
    private String number;

    /**
     * 姓名
     */
    private String userAccount;
}