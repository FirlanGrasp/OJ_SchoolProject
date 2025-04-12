package com.yupi.yuoj.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yupi.yuoj.common.BaseResponse;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.common.ResultUtils;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.model.dto.test.TestAddRequest;
import com.yupi.yuoj.model.dto.test.TestQuestionsRequest;
import com.yupi.yuoj.model.dto.test.TestUpdateRequest;
import com.yupi.yuoj.model.entity.*;
import com.yupi.yuoj.model.vo.TestPageVO;
import com.yupi.yuoj.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 测验接口
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Resource
    private QuestionService questionService;

    @Resource
    private TestService testService;

    @Resource
    private TestQuestionService testQuestionService;

    @Resource
    private UserService userService;


    private final static Gson GSON = new Gson();


    /**
     * 增加测验数据
     * @param testAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Long> addTestUsingPost(@RequestBody TestAddRequest testAddRequest, HttpServletRequest request) {
        //判断测验表的信息是否为空
        if (testAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"测验表信息为空！");
        }
        //将信息复制给Test实体类
        Test test = new Test();
        BeanUtils.copyProperties(testAddRequest, test);

        //判断问题是否空
        List<TestQuestionsRequest> questions = testAddRequest.getQuestions();
        List<Long> questionIds;
        //判断问题是否为空
        if (questions != null && !questions.isEmpty()) {
            questionIds = questions.stream().map(TestQuestionsRequest::getId).collect(Collectors.toList());
        }else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"无测验题目，无法创建测试！");
        }

        if (!questionIds.isEmpty()){
            //判断是否在问题表里面
            long count = questionService.count(new QueryWrapper<Question>()
                    .in("id", questionIds));
            if(count <=0 ) throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在测试题目不在题库中，无法创建测试！");
        }else{
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"无测验题目，无法创建测试！");
        }

        //获取用户信息
        User loginUser = userService.getLoginUser(request);

        //将测试信息存入测试表
            // 将列表转换为JSON字符串
        String jsonQuestionIds = GSON.toJson(questionIds);
        test.setQuestionsId(jsonQuestionIds);
        test.setCreatUserId(loginUser.getId());
        if(!testService.save(test)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"存入测验信息失败！");
        }

        //将问题信息存入测试问题关联表
            //每个进行转换，加入testid
        if(isSaveQuestions(questions, test.getId())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"存入测验关联问题信息失败！");
        }

        return ResultUtils.success(test.getId(),"创建测验成功！");
    }


    /**
     * 更新测验数据
     * @param testUpdateRequest
     * @return
     */
    @PostMapping("/add/update")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Boolean> updateTestUsingPost(@RequestBody TestUpdateRequest testUpdateRequest) {
        //判断测验表的信息是否为空
        if (testUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"测验表信息为空！");
        }

        //检测更新id是否已经存在，不存在返回
        if((testService.getOne(new QueryWrapper<Test>().eq("id",testUpdateRequest.getId()))) == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"无此测验id，无法更新！");
        }

        //将信息复制给Test实体类
        Test test = new Test();
        BeanUtils.copyProperties(testUpdateRequest, test);

        //判断问题是否空
        List<TestQuestionsRequest> questions = testUpdateRequest.getQuestions();
        List<Long> questionIds;
        //判断问题是否为空
        if (questions != null && !questions.isEmpty()) {
            questionIds = questions.stream().map(TestQuestionsRequest::getId).collect(Collectors.toList());
        }else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"无测验题目，无法更新测试！");
        }

        if (!questionIds.isEmpty()){
            //判断是否在问题表里面
            long count = questionService.count(new QueryWrapper<Question>()
                    .in("id", questionIds));
            if(count <=0 ) throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在测试题目不在题库中，无法更新测试！");
        }else{
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"无测验题目，无法更新测试！");
        }

        //将测试信息存入测试表
        // 创建Gson实例
        Gson gson = new Gson();
        // 将列表转换为JSON字符串
        String jsonQuestionIds = gson.toJson(questionIds);
        test.setQuestionsId(jsonQuestionIds);
        testService.updateById(test);

        //将问题信息存入测试问题关联表
        //每个进行转换，加入testid
        testQuestionService.remove(new LambdaQueryWrapper<TestQuestions>().eq(TestQuestions::getTestId, test.getId()));
        if(isSaveQuestions(questions, test.getId())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"存入测验关联问题信息失败！");
        }

        return ResultUtils.success(true,"问题更新成功！");
    }

    /**
     * 将问题信息存入测试关联表
     */
    private boolean isSaveQuestions(List<TestQuestionsRequest> questions, Long testId) {
        List<TestQuestions> testQuestions = questions.stream().map(question -> {
            TestQuestions testQuestionTemp = new TestQuestions();
            BeanUtils.copyProperties(question, testQuestionTemp);
            testQuestionTemp.setQuestionId(question.getId());
            testQuestionTemp.setId(null);
            testQuestionTemp.setTestId(testId);
            return testQuestionTemp;
        }).collect(Collectors.toList());
        return !testQuestionService.saveBatch(testQuestions);
    }

    /**
     * 获取测验信息
     * @param number
     * @return
     */
    @PostMapping("/get")
    public BaseResponse<TestAddRequest> getTestById(@RequestParam long number){
        Test test = testService.getById(number);
        List<Long> questionIds = GSON.fromJson(test.getQuestionsId(), new TypeToken<List<Long>>(){}.getType());
        List<TestQuestionsRequest> testQuestionsRequestStream = questionIds.stream().map(questionId -> {
            TestQuestions question = testQuestionService.getOne(new LambdaQueryWrapper<TestQuestions>().eq(TestQuestions::getQuestionId, questionId));
            TestQuestionsRequest testQuestionsRequest = new TestQuestionsRequest();
            BeanUtils.copyProperties(question, testQuestionsRequest);
            testQuestionsRequest.setId(question.getQuestionId());
            return testQuestionsRequest;
        }).collect(Collectors.toList());
        TestAddRequest testAddRequest = new TestAddRequest();
        BeanUtils.copyProperties(test, testAddRequest);

        testAddRequest.setQuestions(testQuestionsRequestStream);
        return ResultUtils.success(testAddRequest);
    }

    /**
     * 分页查询 测验信息
     * @param current
     * @param pageSize
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<IPage<TestPageVO>> listTestByPage(@RequestParam int current, @RequestParam int pageSize){
        // 查询原始数据
        Page<Test> testPage = testService.page(new Page<>(current, pageSize));

        // 转换为 VO
        IPage<TestPageVO> testPageVOPage = testPage.convert(test -> {
            TestPageVO testPageVO = new TestPageVO();
            BeanUtils.copyProperties(test, testPageVO);
            testPageVO.setUserId(test.getCreatUserId());
            return testPageVO;
        });

        return ResultUtils.success(testPageVOPage);
    }

    /**
     * 删除测验信息
     */
    @PostMapping("/delete")
    public BaseResponse<TestAddRequest> deleteTestById(@RequestParam long number){
        testService.removeById(number);
        testQuestionService.remove(new LambdaQueryWrapper<TestQuestions>().eq(TestQuestions::getTestId, number));
        return ResultUtils.success("删除测验信息成功！");
    }
}


