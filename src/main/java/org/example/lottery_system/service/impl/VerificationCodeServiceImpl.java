package org.example.lottery_system.service.impl;

import org.example.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.example.lottery_system.common.exception.ServiceException;
import org.example.lottery_system.common.utils.*;
import org.example.lottery_system.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    // 对于redis里面的key需要标准化：为了区分业务，应该给key定义前缀：
    // VerificationCode_13111111111:1233、User_13111111111:userInfo

    private static final String VERIFICATION_CODE_PREFIX = "VERIFICATION_CODE_";
    private static final Long VERIFICATION_CODE_TIMEOUT = 60L;
    private static final String VERIFICATION_CODE_TEMPLATE_CODE = "SMS_465324787";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SMSUtil smsUtil;

    /**
     * 向用户提供的手机号发送验证码
     * @param phoneNumber
     */
    @Override
    public void sendVerificationCode(String phoneNumber) {
        // 1.校验手机号
        if (!RegexUtil.checkMobile(phoneNumber)) {
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_ERROR);
        }

        // 2.利用Hutool创建随机验证码
        String code = CaptchaUtil.getCaptcha(4);

        // 3.发送验证码
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        smsUtil.sendMessage(
                VERIFICATION_CODE_TEMPLATE_CODE,
                phoneNumber,
                JacksonUtil.writeValueAsString(map));

        // 4.将随机产生的验证码存入redis
        // 131xxxxxxxx: code
        redisUtil.set(VERIFICATION_CODE_PREFIX + phoneNumber, code, VERIFICATION_CODE_TIMEOUT);
    }

    @Override
    public String getVerificationCode(String phoneNumber) {
        // 校验手机号
        if (!RegexUtil.checkMobile(phoneNumber)) {
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_ERROR);
        }

        return redisUtil.get(VERIFICATION_CODE_PREFIX + phoneNumber);
    }
}
