package com.yupi.yuoj.model.dto.test;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 设置测验可见班级请求
 */
@Data
public class TestSetClassesRequest implements Serializable {

    /**
     * 测验ID
     */
    private Long testId;

    /**
     * 班级ID数组（如果为空数组，表示不选择任何班级）
     */
    private List<Long> classIds;

    private static final long serialVersionUID = 1L;
}
