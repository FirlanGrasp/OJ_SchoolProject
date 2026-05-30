package com.yupi.yuoj.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 班级视图（脱敏）
 */
@Data
public class ClassesVO implements Serializable {

    /**
     * 班级ID
     */
    private Long id;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    private static final long serialVersionUID = 1L;
}