package com.yupi.yuoj.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 班级列表视图（脱敏）
 */
@Data
public class ClassesListVO implements Serializable {

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
    private List<StudentVO> students;

    private static final long serialVersionUID = 1L;
}