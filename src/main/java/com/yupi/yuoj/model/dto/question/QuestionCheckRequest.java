package com.yupi.yuoj.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * 判断题目是否被测验使用的请求
 */
@Data
public class QuestionCheckRequest implements Serializable {

    /**
     * 题目ID
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}

