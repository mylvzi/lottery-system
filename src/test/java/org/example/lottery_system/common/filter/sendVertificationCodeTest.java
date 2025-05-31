package org.example.lottery_system.common.filter;

import org.example.lottery_system.service.impl.VerificationCodeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class sendVertificationCodeTest {
    @Autowired
    private VerificationCodeServiceImpl verificationCodeService;

    @Test
    void sendCodeTest() {
        verificationCodeService.sendVerificationCode("15129270506");
    }
    @Test
    void testSendAndGet() {
        verificationCodeService.sendVerificationCode("15129270502");
        System.out.println(verificationCodeService.getVerificationCode("15129270502"));
    }
}
