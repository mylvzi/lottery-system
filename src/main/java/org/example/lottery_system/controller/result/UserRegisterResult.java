package org.example.lottery_system.controller.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 注册接口返回结果
 * 约定的前后端接口，此处只用返回userid即可
 * 需要通过http传输--》实现序列化和反序列化接口
 */
@Data
public class UserRegisterResult implements Serializable {
    private Long userId;
}
