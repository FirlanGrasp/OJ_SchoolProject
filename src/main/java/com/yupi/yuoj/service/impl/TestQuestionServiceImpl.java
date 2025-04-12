package com.yupi.yuoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yuoj.mapper.TestQuestionMapper;
import com.yupi.yuoj.model.entity.TestQuestions;
import com.yupi.yuoj.service.TestQuestionService;
import org.springframework.stereotype.Service;

/**
* @author Firlan
* @description 针对表【test_question(测验表)】的数据库操作Service实现
* @createDate 2025-03-30 16:20:20
*/
@Service
public class TestQuestionServiceImpl extends ServiceImpl<TestQuestionMapper, TestQuestions>
    implements TestQuestionService {

}




