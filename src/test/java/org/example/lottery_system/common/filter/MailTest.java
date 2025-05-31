package org.example.lottery_system.common.filter;

import org.example.lottery_system.common.utils.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailTest {

    @Autowired
    private MailUtil mailUtil;

    @Test
    void sendMessage() {
        String context = "短信发送测试";
        mailUtil.sendSampleMail("2121209177@qq.com", "标题", context);
    }

}