package com.yupi.yuoj.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 班级学生关联表
 * @TableName class_student
 */
@TableName(value ="class_student")
@Data
public class ClassStudent implements Serializable {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 班级ID
     */
    @TableField(value = "classId")
    private Long classId;

    /**
     * 学生ID
     */
    @TableField(value = "studentId")
    private Long studentId;

    /**
     * 学生真实姓名
     */
    @TableField(value = "userAccount")
    private String userAccount;

    /**
     * 学生学号
     */
    @TableField(value = "number")
    private String number;

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
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}