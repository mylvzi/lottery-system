package org.example.lottery_system.service;

import org.example.lottery_system.controller.param.UserLoginParam;
import org.example.lottery_system.controller.param.UserRegisterParam;
import org.example.lottery_system.service.dto.UserDTO;
import org.example.lottery_system.service.dto.UserLoginDTO;
import org.example.lottery_system.service.dto.UserRegisterDTO;
import org.example.lottery_system.service.enums.UserIdentityEnum;

import java.util.List;

public interface UserService {
    /**
     * 用户注册
     */
    UserRegisterDTO register(UserRegisterParam param);

    /**
     * 用户登录
     *   1、 密码
     *   2、 验证码
     *
     * @param param
     * @return
     */
    UserLoginDTO login(UserLoginParam param);

    /**
     * 根据身份查询人员列表
     *
     * @param identity: 如果为空，查询各个身份人员列表
     * @return
     */
    List<UserDTO> findUserInfo(UserIdentityEnum identity);
}
