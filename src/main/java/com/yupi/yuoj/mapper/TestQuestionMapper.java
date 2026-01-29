package com.yupi.yuoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.yuoj.model.entity.TestQuestions;
import org.apache.ibatis.annotations.Param;

/**
* 针对表【test_question(测验表)】的数据库操作Mapper
*/
public interface TestQuestionMapper extends BaseMapper<TestQuestions> {

    /**
     * 统计某个题目被多少个测验关联（仅统计未删除记录）
     */
    int countByQuestionId(@Param("questionId") Long questionId);
}




