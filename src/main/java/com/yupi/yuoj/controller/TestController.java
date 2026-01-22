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
import com.yupi.yuoj.model.dto.test.TestGetClassesRequest;
import com.yupi.yuoj.model.dto.test.TestQuestionsRequest;
import com.yupi.yuoj.model.dto.test.TestSetClassesRequest;
import com.yupi.yuoj.model.dto.test.TestUpdateRequest;
import com.yupi.yuoj.model.entity.*;
import com.yupi.yuoj.model.vo.TestPageVO;
import com.yupi.yuoj.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    @Resource
    private TestClassService testClassService;

    @Resource
    private ClassService classService;

    @Resource
    private ClassStudentService classStudentService;

    private final static Gson GSON = new Gson();

    /**
     * 增加测验数据
     * 
     * @param testAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Long> addTestUsingPost(@RequestBody TestAddRequest testAddRequest, HttpServletRequest request) {
        // 判断测验表的信息是否为空
        if (testAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "测验表信息为空！");
        }
        // 将信息复制给Test实体类
        Test test = new Test();
        BeanUtils.copyProperties(testAddRequest, test);

        // 判断问题是否空
        List<TestQuestionsRequest> questions = testAddRequest.getQuestions();
        List<Long> questionIds;
        // 判断问题是否为空
        if (questions != null && !questions.isEmpty()) {
            questionIds = questions.stream().map(TestQuestionsRequest::getId).collect(Collectors.toList());
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无测验题目，无法创建测试！");
        }

        if (!questionIds.isEmpty()) {
            // 判断是否在问题表里面
            long count = questionService.count(new QueryWrapper<Question>()
                    .in("id", questionIds));
            if (count <= 0)
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在测试题目不在题库中，无法创建测试！");
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无测验题目，无法创建测试！");
        }

        // 获取用户信息
        User loginUser = userService.getLoginUser(request);

        // 将测试信息存入测试表
        // 将列表转换为JSON字符串
        String jsonQuestionIds = GSON.toJson(questionIds);
        test.setQuestionsId(jsonQuestionIds);
        test.setCreatUserId(loginUser.getId());
        if (!testService.save(test)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "存入测验信息失败！");
        }

        // 将问题信息存入测试问题关联表
        // 每个进行转换，加入testid
        if (isSaveQuestions(questions, test.getId())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "存入测验关联问题信息失败！");
        }

        return ResultUtils.success(test.getId(), "创建测验成功！");
    }

    /**
     * 更新测验数据
     * 
     * @param testUpdateRequest
     * @return
     */
    @PostMapping("/add/update")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Boolean> updateTestUsingPost(@RequestBody TestUpdateRequest testUpdateRequest) {
        // 判断测验表的信息是否为空
        if (testUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "测验表信息为空！");
        }

        // 检测更新id是否已经存在，不存在返回
        if ((testService.getOne(new QueryWrapper<Test>().eq("id", testUpdateRequest.getId()))) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无此测验id，无法更新！");
        }

        // 将信息复制给Test实体类
        Test test = new Test();
        BeanUtils.copyProperties(testUpdateRequest, test);

        // 判断问题是否空
        List<TestQuestionsRequest> questions = testUpdateRequest.getQuestions();
        List<Long> questionIds;
        // 判断问题是否为空
        if (questions != null && !questions.isEmpty()) {
            questionIds = questions.stream().map(TestQuestionsRequest::getId).collect(Collectors.toList());
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无测验题目，无法更新测试！");
        }

        if (!questionIds.isEmpty()) {
            // 判断是否在问题表里面
            long count = questionService.count(new QueryWrapper<Question>()
                    .in("id", questionIds));
            if (count <= 0)
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在测试题目不在题库中，无法更新测试！");
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无测验题目，无法更新测试！");
        }

        // 将测试信息存入测试表
        // 创建Gson实例
        Gson gson = new Gson();
        // 将列表转换为JSON字符串
        String jsonQuestionIds = gson.toJson(questionIds);
        test.setQuestionsId(jsonQuestionIds);
        testService.updateById(test);

        // 将问题信息存入测试问题关联表
        // 每个进行转换，加入testid
        testQuestionService.remove(new LambdaQueryWrapper<TestQuestions>().eq(TestQuestions::getTestId, test.getId()));
        if (isSaveQuestions(questions, test.getId())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "存入测验关联问题信息失败！");
        }

        return ResultUtils.success(true, "问题更新成功！");
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
     * 
     * @param number
     * @return
     */
    @PostMapping("/get")
    public BaseResponse<TestAddRequest> getTestById(@RequestParam long number) {
        Test test = testService.getById(number);
        List<TestQuestions> questions = testQuestionService
                .list(new LambdaQueryWrapper<TestQuestions>().eq(TestQuestions::getTestId, number));
        List<TestQuestionsRequest> testQuestionsRequestStream = questions.stream().map(question -> {
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
     * 分页查询测验信息（支持按标题模糊匹配，自动按班级过滤）
     * 
     * @param current  当前页
     * @param pageSize 每页大小
     * @param title    可选标题关键词（匹配测验标题）
     * @param request  HTTP请求，用于获取当前登录用户
     */
    @PostMapping("/list/page")
    public BaseResponse<IPage<TestPageVO>> listTestByPage(
            @RequestParam int current,
            @RequestParam int pageSize,
            @RequestParam(required = false) String title,
            HttpServletRequest request) {

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);

        // 查询当前用户所在的班级ID列表
        List<ClassStudent> classStudentList = classStudentService.list(
                new LambdaQueryWrapper<ClassStudent>()
                        .eq(ClassStudent::getStudentId, loginUser.getId()));

        // 如果用户不在任何班级，返回空列表
        if (classStudentList == null || classStudentList.isEmpty()) {
            Page<TestPageVO> emptyPage = new Page<>(current, pageSize, 0);
            return ResultUtils.success(emptyPage);
        }

        // 提取班级ID列表
        List<Long> classIds = classStudentList.stream()
                .map(ClassStudent::getClassId)
                .distinct()
                .collect(Collectors.toList());

        // 根据班级ID查询关联的测验ID列表
        List<TestClass> testClassList = testClassService.list(
                new LambdaQueryWrapper<TestClass>()
                        .in(TestClass::getClassId, classIds));

        // 如果没有任何关联的测验，返回空列表
        if (testClassList == null || testClassList.isEmpty()) {
            Page<TestPageVO> emptyPage = new Page<>(current, pageSize, 0);
            return ResultUtils.success(emptyPage);
        }

        // 提取测验ID列表
        List<Long> testIds = testClassList.stream()
                .map(TestClass::getTestId)
                .distinct()
                .collect(Collectors.toList());

        // 构建查询条件
        QueryWrapper<Test> queryWrapper = new QueryWrapper<>();

        // 添加标题模糊匹配条件（如果参数不为空）
        if (StringUtils.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }

        // 添加测验ID过滤条件（只查询与用户班级关联的测验）
        queryWrapper.in("id", testIds);

        // 构建分页查询条件
        Page<Test> testPage = testService.page(
                new Page<>(current, pageSize),
                queryWrapper);

        // 转换为VO（保持原有逻辑）
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
    public BaseResponse<TestAddRequest> deleteTestById(@RequestParam long number) {
        testService.removeById(number);
        testQuestionService.remove(new LambdaQueryWrapper<TestQuestions>().eq(TestQuestions::getTestId, number));
        return ResultUtils.success("删除测验信息成功！");
    }

    /**
     * 创建设置测验的可见班级（批量设置）
     * 
     * @param testSetClassesRequest
     * @return
     */
    @PostMapping("/setClasses")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Boolean> setTestClassesUsingPost(@RequestBody TestSetClassesRequest testSetClassesRequest) {
        // 判断请求参数是否为空
        if (testSetClassesRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空！");
        }

        Long testId = testSetClassesRequest.getTestId();
        List<Long> classIds = testSetClassesRequest.getClassIds();

        // 验证测验ID
        if (testId == null || testId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "测验ID不能为空！");
        }

        // 验证测验是否存在
        Test test = testService.getById(testId);
        if (test == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "测验不存在！");
        }

        // 如果班级ID列表不为空，验证班级是否存在
        if (classIds != null && !classIds.isEmpty()) {
            // 验证班级ID是否有效
            long validClassCount = classService.count(new QueryWrapper<Classes>()
                    .in("id", classIds));
            if (validClassCount != classIds.size()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在无效的班级ID！");
            }
        }

        // 删除该测验的所有现有班级关联
        testClassService.remove(new LambdaQueryWrapper<TestClass>()
                .eq(TestClass::getTestId, testId));

        // 如果班级ID列表不为空，批量插入新的关联
        if (classIds != null && !classIds.isEmpty()) {
            List<TestClass> testClassList = classIds.stream().map(classId -> {
                TestClass testClass = new TestClass();
                testClass.setTestId(testId);
                testClass.setClassId(classId);
                return testClass;
            }).collect(Collectors.toList());

            if (!testClassService.saveBatch(testClassList)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "保存测验班级关联失败！");
            }
        }

        return ResultUtils.success(true, "设置测验可见班级成功！");
    }

    /**
     * 查询测验的可见班级列表
     * 
     * @param testGetClassesRequest
     * @return
     */
    @PostMapping("/getClasses")
    public BaseResponse<List<Long>> getClassesByTestUsingPost(
            @RequestBody TestGetClassesRequest testGetClassesRequest) {
        // 判断请求参数是否为空
        if (testGetClassesRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空！");
        }

        Long testId = testGetClassesRequest.getTestId();

        // 验证测验ID
        if (testId == null || testId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "测验ID不能为空！");
        }

        // 验证测验是否存在
        Test test = testService.getById(testId);
        if (test == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "测验不存在！");
        }

        // 查询该测验关联的所有班级ID
        List<TestClass> testClassList = testClassService.list(new LambdaQueryWrapper<TestClass>()
                .eq(TestClass::getTestId, testId));

        // 提取班级ID列表
        List<Long> classIds = testClassList.stream()
                .map(TestClass::getClassId)
                .collect(Collectors.toList());

        return ResultUtils.success(classIds, "查询成功！");
    }
}
