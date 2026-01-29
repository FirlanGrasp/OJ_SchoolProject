package com.yupi.yuoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.yuoj.model.entity.ClassStudent;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
* @author localAccount
* @description 针对表【class_student(班级学生关联表)】的数据库操作Mapper
* @createDate 2025-05-30 15:06:30
*/
public interface ClassStudentMapper extends BaseMapper<ClassStudent> {

    /**
     * 查询指定学生列表的历史班级关联（逻辑删除记录，isDelete = 1）
     * 直接映射到自定义 SQL，绕过 MyBatis-Plus 的逻辑删除过滤
     */
    List<ClassStudent> listRemovedByStudentIds(@Param("studentIds") List<Long> studentIds);
}




