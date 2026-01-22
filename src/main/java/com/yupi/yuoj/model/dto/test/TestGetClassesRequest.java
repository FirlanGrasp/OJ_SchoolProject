package com.yupi.yuoj.model.dto.test;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询测验可见班级请求
 */
@Data
public class TestGetClassesRequest implements Serializable {

    /**
     * 测验ID
     */
    private Long testId;

    private static final long serialVersionUID = 1L;
}
