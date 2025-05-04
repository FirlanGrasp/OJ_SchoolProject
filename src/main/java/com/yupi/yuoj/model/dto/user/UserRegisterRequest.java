package com.yupi.yuoj.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户注册请求体
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 账户ID/学号  （默认密码）
     */
    private String id;

    /**
     * 昵称，可选
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String userAccount;

    /**
     * 用户角色
     */
    private String userRole;

//    private String userPassword;

//    private String checkPassword;

}
