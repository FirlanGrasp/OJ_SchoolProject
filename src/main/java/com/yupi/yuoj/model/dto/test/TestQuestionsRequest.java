package com.yupi.yuoj.model.dto.test;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 测试问题列表请求
 *
 */
@Data
public class TestQuestionsRequest implements Serializable {

    /**
     * 题目ID
     */
    private Long id;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目分值
     */
    private int score;

    /**
     * 题目类型
     */
    private String type;

    private static final long serialVersionUID = 1L;
}