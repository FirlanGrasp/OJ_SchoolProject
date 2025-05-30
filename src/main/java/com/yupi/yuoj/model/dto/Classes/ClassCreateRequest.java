package com.yupi.yuoj.model.dto.Classes;

import lombok.Data;

import java.util.List;

@Data
public class ClassCreateRequest {
    /**
     * 班级名称
     */
    private String className;

    /**
     * 学生学号
     */
    private List<String> studentIds;
}