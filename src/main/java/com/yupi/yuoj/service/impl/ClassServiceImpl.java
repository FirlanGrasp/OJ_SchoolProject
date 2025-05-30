package com.yupi.yuoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yuoj.mapper.ClassMapper;
import com.yupi.yuoj.model.entity.Classes;
import com.yupi.yuoj.service.ClassService;
import org.springframework.stereotype.Service;
/**
 * @author localAccount
 * @description 针对表【class(班级表)】的数据库操作Service实现
 * @createDate 2025-05-30 14:48:51
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Classes>
        implements ClassService {

}

