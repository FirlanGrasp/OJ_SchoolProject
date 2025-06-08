package com.yupi.yuoj.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.google.gson.Gson;
import com.yupi.yuoj.common.BaseResponse;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.common.ResultUtils;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.model.dto.Classes.ClassCreateRequest;
import com.yupi.yuoj.model.entity.*;
import com.yupi.yuoj.model.vo.ClassesListVO;
import com.yupi.yuoj.model.vo.ClassesVO;
import com.yupi.yuoj.model.vo.StudentVO;
import com.yupi.yuoj.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 班级相关接口
 */
@RestController
@RequestMapping("/class")
@Slf4j
public class ClassController {

    @Resource
    private UserService userService;

    @Resource
    private ClassService classService;

    @Resource
    private ClassStudentService classStudentService;

    private final static Gson GSON = new Gson();

    /**
     * 创建班级
     * @param request
     * @return
     */
    @PostMapping("/create")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<ClassesVO> createClass(@RequestBody ClassCreateRequest request) {
        if (request == null || StringUtils.isEmpty(request.getClassName())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }

        // 校验学生ID是否存在
        if (CollectionUtil.isNotEmpty(request.getNumbers())) {
            for (String number : request.getNumbers()) {
                User student = userService.getOne(new QueryWrapper<User>().eq("number", number));
                if (student == null || student.getIsDelete() == 1) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "有学生不存在");
                }
                if (!"student".equals(student.getUserRole())) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在用户不是学生");
                }
            }
        }

        // 创建班级
        Classes classEntity = new Classes();
        classEntity.setClassName(request.getClassName());
        boolean save = classService.save(classEntity);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "班级创建失败！");
        }

        // 添加学生到班级
        if (CollectionUtils.isNotEmpty(request.getNumbers())) {
            List<ClassStudent> classStudentList = request.getNumbers().stream()
                    .map( number -> {
                        User student = userService.getOne(new QueryWrapper<User>().eq("number", number));
                        ClassStudent classStudent = new ClassStudent();
                        classStudent.setClassId(classEntity.getId());
                        classStudent.setStudentId(student.getId());
                        classStudent.setUserAccount(student.getUserAccount());
                        classStudent.setNumber(number);
                        return classStudent;
                    }).collect(Collectors.toList());
            classStudentService.saveBatch(classStudentList);
        }

        // 返回班级信息
        ClassesVO classesVO = new ClassesVO();
        BeanUtils.copyProperties(classService.getById(classEntity.getId()), classesVO);
        return ResultUtils.success(classesVO);
    }

    /**
     * 查看所有班级信息
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<List<ClassesListVO>> listAllClasses() {
        // 查询所有未删除的班级
        List<Classes> classList = classService.list();

        if (CollectionUtils.isEmpty(classList)) {
            return ResultUtils.success(Collections.emptyList());
        }

        //每一个班级都创建一个vo，进行属性的复制，然后封装学生信息，进行vo赋值
        List<ClassesListVO> result = new ArrayList<>();
        for (Classes classEntity : classList) {
            ClassesListVO classesListVO = new ClassesListVO();
            BeanUtils.copyProperties(classEntity, classesListVO);
            Long classEntityId = classEntity.getId();
            List<ClassStudent> classStudentList = classStudentService.lambdaQuery().eq(ClassStudent::getClassId, classEntityId).list();
            if (CollectionUtils.isNotEmpty(classStudentList)) {
                List<StudentVO> studentVOList = classStudentList.stream().map(student -> {
                    StudentVO studentVO = new StudentVO();
                    BeanUtils.copyProperties(student, studentVO);
                    return studentVO;
                }).collect(Collectors.toList());
                classesListVO.setStudents(studentVOList);
            }
            result.add(classesListVO);
        }

        return ResultUtils.success(result);
    }

    /**
     * 删除班级
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<?> deleteClassUsingPost(String id) {
        classService.removeById(id);
        classStudentService.remove(new LambdaQueryWrapper<ClassStudent>().eq(ClassStudent::getClassId, id));
        return ResultUtils.success("删除成功");
    }

}


