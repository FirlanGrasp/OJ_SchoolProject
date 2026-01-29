package com.yupi.yuoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yuoj.model.entity.TestQuestions;

/**
* 针对表【test_question(测验表)】的数据库操作Service
*/
public interface TestQuestionService extends IService<TestQuestions> {

    /**
     * 判断题目是否被任意测验使用
     *
     * @param questionId 题目ID
     * @return true 表示已被测验关联
     */
    boolean isQuestionUsedByTests(Long questionId);
}
