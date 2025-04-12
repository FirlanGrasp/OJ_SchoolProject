package com.yupi.yuoj.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 测验表
 * @TableName test_question
 */
@TableName(value ="test_question")
@Data
public class TestQuestions implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 测验id
     */
    @TableField(value = "testId")
    private Long testId;

    /**
     * 题目id
     */
    @TableField(value = "questionId")
    private Long questionId;

    /**
     * 题目标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 题目分值
     */
    @TableField(value = "score")
    private Integer score;

    /**
     * 题目类型
     */
    @TableField(value = "type")
    private String type;

    /**
     * 是否删除
     */
    @TableField(value = "isDelete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}