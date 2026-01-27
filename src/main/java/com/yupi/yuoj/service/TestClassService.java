package com.yupi.yuoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yuoj.model.entity.TestClass;

/**
* @author localAccount
* @description 针对表【test_class(测验-班级关联表)】的数据库操作Service
* @createDate 2025-01-XX XX:XX:XX
*/
public interface TestClassService extends IService<TestClass> {

    /**
     * 物理删除：根据 testId 删除关联记录（不走逻辑删除）
     */
    boolean removeByTestIdPhysical(Long testId);
}
