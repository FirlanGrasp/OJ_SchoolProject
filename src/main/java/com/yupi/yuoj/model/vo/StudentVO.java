package com.yupi.yuoj.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 学生视图（脱敏）
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class StudentVO implements Serializable {

    /**
     * 学会生ID
     */
    private Long id;

    /**
     * 学生学号
     */
    private String number;

    /**
     * 学生姓名
     */
    private String userAccount;

    private static final long serialVersionUID = 1L;
}