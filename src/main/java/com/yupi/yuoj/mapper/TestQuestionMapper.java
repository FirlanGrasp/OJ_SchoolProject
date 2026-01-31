package com.yupi.yuoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.yuoj.model.entity.TestQuestions;
import org.apache.ibatis.annotations.Param;

/**
 * 针对表【test_question(测验表)】的数据库操作Mapper
 */
public interface TestQuestionMapper extends BaseMapper<TestQuestions> {

    /**
     * 统计某个题目被多少个未删除的测验关联（仅统计 test_question 未删除且测验 test 未删除）
     */
    int countByQuestionId(@Param("questionId") Long questionId);
}
