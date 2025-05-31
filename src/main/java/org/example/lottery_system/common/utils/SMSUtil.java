package org.example.lottery_system.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SMSUtil {
    private static final Logger logger = LoggerFactory.getLogger(SMSUtil.class);

    /**
     * 模拟发送短信
     *
     * @param templateCode 模板号
     * @param phoneNumbers 手机号
     * @param templateParam 模板参数 {"key":"value"}
     */
    public void sendMessage(String templateCode, String phoneNumbers, String templateParam) {
        // 模拟输出，不真正发送短信
        logger.info("【模拟短信发送】手机号：{}，模板Code：{}，验证码内容：{}", phoneNumbers, templateCode, templateParam);
        System.out.println("【模拟短信】发送给手机号：" + phoneNumbers + "，验证码内容：" + templateParam);
    }
}
