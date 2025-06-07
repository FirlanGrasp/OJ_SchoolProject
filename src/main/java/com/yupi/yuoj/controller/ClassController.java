package com.yupi.yuoj.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.yupi.yuoj.common.BaseResponse;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.common.ResultUtils;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.model.dto.Classes.ClassCreateRequest;
import com.yupi.yuoj.model.entity.*;
import com.yupi.yuoj.model.vo.ClassesVO;
import com.yupi.yuoj.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
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


    @PostMapping("/class")
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

}


