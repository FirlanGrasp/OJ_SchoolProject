package com.yupi.yuoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yuoj.mapper.ClassStudentMapper;
import com.yupi.yuoj.model.entity.ClassStudent;
import com.yupi.yuoj.service.ClassStudentService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author localAccount
 * @description 针对表【class_student(班级学生表)】的数据库操作Service实现
 * @createDate 2025-05-30 14:48:51
 */
@Service
public class ClassStudentServiceImpl extends ServiceImpl<ClassStudentMapper, ClassStudent>
        implements ClassStudentService {

    @Override
    public List<ClassStudent> listRemovedByStudentIds(List<Long> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) {
            return new ArrayList<>();
        }
        return this.baseMapper.listRemovedByStudentIds(studentIds);
    }
}

