package org.example.lottery_system.controller.param;

import lombok.Data;
import org.example.lottery_system.service.enums.UserIdentityEnum;

import java.io.Serializable;

@Data
public class UserLoginParam implements Serializable {

    /**
     * 强制某身份登录。不填不限制身份
     * @see UserIdentityEnum#name()
     */
    private String mandatoryIdentity;

}