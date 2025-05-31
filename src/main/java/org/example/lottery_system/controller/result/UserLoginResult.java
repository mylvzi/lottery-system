package org.example.lottery_system.controller.result;

import lombok.Data;

@Data
public class UserLoginResult {

    /**
     * JWT 令牌
     */
    private String token;

    /**
     * 登录人员身份
     */
    private String identity;
}
