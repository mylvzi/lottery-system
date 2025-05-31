package org.example.lottery_system.controller.param;

import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterParam implements Serializable {
    /**
     * 姓名  如果为空就进行提示
     */
    @NotBlank(message = "姓名不能为空！")
    private String name;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空！")
    private String mail;

    /**
     * 电话
     */
    @NotBlank(message = "电话不能为空！")
    private String phoneNumber;

    /**
     * 密码
     * 管理员注册需要密码
     * 管理员添加用户不需要注册
     */
    private String password;

    /**
     * 身份信息
     */
    @NotBlank(message = "身份信息不能为空！")
    private String identity;
}
