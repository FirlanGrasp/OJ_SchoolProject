package com.yupi.yuoj.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 测验表
 * @TableName test
 */
@TableName(value ="test")
@Data
public class Test implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 测试标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 测验描述(Markdouwn 格式)
     */
    @TableField(value = "content")
    private String content;

    /**
     * 开始时间(格式: YYYY-MM-DD HH:mm)
     */
    @TableField(value = "startTime")
    private String startTime;

    /**
     * 结束时间(格式: YYYY-MM-DD HH:mm)
     */
    @TableField(value = "endTime")
    private String endTime;

    /**
     * 是否对学生可见
     */
    @TableField(value = "studentVisible")
    private Boolean studentVisible;

    /**
     * 是否显示排行榜
     */
    @TableField(value = "rankVisible")
    private Boolean rankVisible;


    /**
     * 是否支持分享
     */
    @TableField(value = "codeShare")
    private Boolean codeShare;


    /**
     * 测验类型(false=测验，true=考试)
     */
    @TableField(value = "examType")
    private Boolean examType;

    /**
     * 总分要求(默认100分)
     */
    @TableField(value = "totalRequiredScore")
    private Integer totalRequiredScore;

    /**
     * 题目id
     */
    @TableField(value = "questionsId")
    private String questionsId;

    /**
     * 创建用户 id
     */
    @TableField(value = "creatUserId")
    private Long creatUserId;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "isDelete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}