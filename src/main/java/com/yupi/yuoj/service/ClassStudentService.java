package com.yupi.yuoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yuoj.model.entity.ClassStudent;
import java.util.List;

/**
* @author localAccount
* @description 针对表【class_student(班级学生关联表)】的数据库操作Service
* @createDate 2025-05-30 15:06:30
*/
public interface ClassStudentService extends IService<ClassStudent> {

    /**
     * 查询指定学生列表的历史班级关联（isDelete = 1）
     */
    List<ClassStudent> listRemovedByStudentIds(List<Long> studentIds);
}
