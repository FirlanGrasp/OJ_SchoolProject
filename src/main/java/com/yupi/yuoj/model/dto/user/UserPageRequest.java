package com.yupi.yuoj.model.dto.user;

import lombok.Data;

import java.io.Serializable;

// 请求参数类
@Data
public class UserPageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 当前页码
     */
    private Long current;
    
    /**
     * 每页条数
     */
    private Long pageSize;
    
    /**
     * 用户角色（固定为student）
     */
    private String userRole = "student";
}