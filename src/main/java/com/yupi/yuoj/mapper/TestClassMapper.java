package com.yupi.yuoj.mapper;

import com.yupi.yuoj.model.entity.TestClass;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author localAccount
 * @description 针对表【test_class(测验-班级关联表)】的数据库操作Mapper
 * @createDate 2025-01-XX XX:XX:XX
 */
public interface TestClassMapper extends BaseMapper<TestClass> {

    /**
     * 物理删除：根据 testId 直接 DELETE
     */
    int deleteByTestIdPhysical(@Param("testId") Long testId);

}
