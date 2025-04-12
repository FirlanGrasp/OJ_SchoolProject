package com.yupi.yuoj.model.vo;


import lombok.Data;

import java.util.Date;

@Data
public class TestPageVO {
    /**
     * id
     */
    private Long id;

    /**
     * 测试标题
     */
    private String title;

    /**
     * 测验描述(Markdouwn 格式)
     */
    private String content;

    /**
     * 开始时间(格式: YYYY-MM-DD HH:mm)
     */
    private String startTime;

    /**
     * 结束时间(格式: YYYY-MM-DD HH:mm)
     */
    private String endTime;

    /**
     * 是否对学生可见
     */
    private Integer studentVisible;

    /**
     * 是否显示排行榜
     */
    private Integer rankVisible;


    /**
     * 测验类型(false=测验，true=考试)
     */
    private Integer examType;

    /**
     * 总分要求(默认100分)
     */
    private Integer totalRequiredScore;

    /**
     * 题目id
     */
    private String questionsId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

}
