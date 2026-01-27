package com.yupi.yuoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yuoj.mapper.TestClassMapper;
import com.yupi.yuoj.model.entity.TestClass;
import com.yupi.yuoj.service.TestClassService;
import org.springframework.stereotype.Service;

/**
 * @author localAccount
 * @description 针对表【test_class(测验-班级关联表)】的数据库操作Service实现
 * @createDate 2025-01-XX XX:XX:XX
 */
@Service
public class TestClassServiceImpl extends ServiceImpl<TestClassMapper, TestClass>
        implements TestClassService {

    /**
     * 物理删除：根据 testId 直接 DELETE，不走逻辑删除
     */
    @Override
    public boolean removeByTestIdPhysical(Long testId) {
        return this.baseMapper.deleteByTestIdPhysical(testId) >= 0;
    }
}
