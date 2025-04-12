package com.yupi.yuoj.model.dto.test;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 测试问题列表请求
 *
 */
@Data
public class TestUpdateRequest implements Serializable {
    /**
     * 测验id
     */
    private Long id;

    /**
     * 测验标题
     */
    private String title;

    /**
     * 测验描述（Markdown格式）
     */
    private String content;

    /**
     * 开始时间（格式：YYYY-MM-DD HH:mm）
     */
    private String startTime;

    /**
     * 结束时间（格式：YYYY-MM-DD HH:mm）
     */
    private String endTime;

    /**
     * 是否对学生可见
     */
    private boolean studentVisible;

    /**
     * 是否显示排行榜
     */
    private boolean rankVisible;

    /**
     * 是否允许代码分享
     */
    private boolean codeShare;

    /**
     * 测验类型（false=测验，true=考试）
     */
    private boolean examType;

    /**
     * 总分要求（默认100分）
     */
    private int totalRequiredScore;

    /**
     * 题目列表
     */
    private List<TestQuestionsRequest> questions;

    private static final long serialVersionUID = 1L;
}