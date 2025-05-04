package com.yupi.yuoj.model.dto.user;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户批量注册响应类
 *
 */
@Data
public class UserRegisterBatchResponse implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 成功导入数量
     */
    private int successCount;

    /**
     * 失败数量
     */
    private int failCount;

    /**
     * 失败详情
     */
    private List<FailDetail> failList = new ArrayList<>();

    /**
     * 失败详情内部类（非静态）
     */
    @Data
    public class FailDetail implements Serializable {
        /**
         * 失败记录ID
         */
        private String id;

        /**
         * 失败原因
         */
        private String reason;
    }

    public void addFailDetail(FailDetail detail) {
        failList.add(detail);
    }


}
