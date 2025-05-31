package org.example.lottery_system.controller;

import org.example.lottery_system.common.errorcode.ControllerErrorCodeConstants;
import org.example.lottery_system.common.exception.ControllerException;
import org.example.lottery_system.common.pojo.CommonResult;
import org.example.lottery_system.common.utils.JacksonUtil;
import org.example.lottery_system.controller.param.ShortMessageLoginParam;
import org.example.lottery_system.controller.param.UserPasswordLoginParam;
import org.example.lottery_system.controller.param.UserRegisterParam;
import org.example.lottery_system.controller.result.BaseUserInfoResult;
import org.example.lottery_system.controller.result.UserLoginResult;
import org.example.lottery_system.controller.result.UserRegisterResult;
import org.example.lottery_system.service.UserService;
import org.example.lottery_system.service.VerificationCodeService;
import org.example.lottery_system.service.dto.UserDTO;
import org.example.lottery_system.service.dto.UserLoginDTO;
import org.example.lottery_system.service.dto.UserRegisterDTO;
import org.example.lottery_system.service.enums.UserIdentityEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    // 设置日志对象
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private VerificationCodeService verificationCodeService;

    /**
     * 注册接口
     */
    // @PostMapping
    @RequestMapping("/register")
    public CommonResult<UserRegisterResult> userRegister(
            @Validated @RequestBody UserRegisterParam param) {
        // 日志打印  先判断传递的参数是否正确
        logger.info("userRegister UserRegisterParam:{}", JacksonUtil.writeValueAsString(param));

        // 调用Service层服务进行访问
        UserRegisterDTO userRegisterDTO = userService.register(param);
        return CommonResult.success(convertToUserRegisterResult(userRegisterDTO));
    }

    /**
     * 发送验证码
     *
     * @param phoneNumber
     * @return
     */
    @RequestMapping("/verification-code/send")
    public CommonResult<Boolean> sendVerificationCode(String phoneNumber) {
        logger.info("sendVerificationCode phoneNumber:{}", phoneNumber);
        verificationCodeService.sendVerificationCode(phoneNumber);
        return CommonResult.success(Boolean.TRUE);
    }

    /**
     * 密码登录
     *
     * @param param
     * @return
     */
    @RequestMapping("/password/login")
    public CommonResult<UserLoginResult> userPasswordLogin(
            @Validated @RequestBody UserPasswordLoginParam param) {
        logger.info("userPasswordLogin UserPasswordLoginParam:{}",
                JacksonUtil.writeValueAsString(param));

        UserLoginDTO userLoginDTO = userService.login(param);
        return CommonResult.success(convertToUserLoginResult(userLoginDTO));

    }

    /**
     * 短信验证码登录
     *
     * @param param
     * @return
     */
    @RequestMapping("/message/login")
    public CommonResult<UserLoginResult> shortMessageLogin(
            @Validated @RequestBody ShortMessageLoginParam param) {
        logger.info("shortMessageLogin ShortMessageLoginParam:{}",
                JacksonUtil.writeValueAsString(param));
        UserLoginDTO userLoginDTO = userService.login(param);
        return CommonResult.success(convertToUserLoginResult(userLoginDTO));
    }

    @RequestMapping("/base-user/find-list")
    public CommonResult<List<BaseUserInfoResult>> findBaseUserInfo(String identity) {
        logger.info("findBaseUserInfo identity:{}", identity);

        // 返回人员列表
        List<UserDTO> userDTOList = userService.findUserInfo(
                UserIdentityEnum.forName(identity));
        return CommonResult.success(convertToList(userDTOList));
    }

    private List<BaseUserInfoResult> convertToList(List<UserDTO> userDTOList) {
        // 空列表
        if (CollectionUtils.isEmpty(userDTOList)) {
            return Arrays.asList();
        }

        return userDTOList.stream()
                .map(userDTO -> {
                    BaseUserInfoResult result = new BaseUserInfoResult();
                    result.setUserId(userDTO.getUserId());
                    result.setUserName(userDTO.getUserName());
                    result.setIdentity(userDTO.getIdentity().name());
                    return result;
                }).collect(Collectors.toList());

    }

    private UserLoginResult convertToUserLoginResult(UserLoginDTO userLoginDTO) {
        if (null == userLoginDTO) {
            throw new ControllerException(ControllerErrorCodeConstants.LOGIN_ERROR);
        }

        UserLoginResult result = new UserLoginResult();
        result.setToken(userLoginDTO.getToken());
        result.setIdentity(userLoginDTO.getIdentity().name());
        return result;
    }

    private UserRegisterResult convertToUserRegisterResult(UserRegisterDTO userRegisterDTO) {
        UserRegisterResult result = new UserRegisterResult();
        if (null == userRegisterDTO) {
            throw new ControllerException(ControllerErrorCodeConstants.REGISTER_ERROR);
        }
        result.setUserId(userRegisterDTO.getUserId());
        return result;
    }


}
