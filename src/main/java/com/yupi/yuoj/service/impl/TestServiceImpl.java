package com.yupi.yuoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yuoj.mapper.TestMapper;
import com.yupi.yuoj.model.entity.Test;
import com.yupi.yuoj.service.TestService;
import org.springframework.stereotype.Service;

/**
* @author Firlan
* @description 针对表【test(测验表)】的数据库操作Service实现
* @createDate 2025-03-30 15:13:28
*/
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test>
    implements TestService {

}




