package org.example.lottery_system.common.filter;

import org.example.lottery_system.common.utils.SMSUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class smsTest {

    @Autowired
    private SMSUtil smsUtil;
    @Test
    void smsTest() {
        smsUtil.sendMessage(
                "SMS_465324787",
                "15129270506",
                "{\"code\":\"1234\"}");
    }
}
