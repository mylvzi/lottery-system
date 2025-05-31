package org.example.lottery_system.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.lottery_system.common.errorcode.ControllerErrorCodeConstants;
import org.example.lottery_system.common.errorcode.ErrorCode;

// @Data会生成自己的equals和hashcode,但是我们要使用父类的
// 使用 @EqualsAndHashCode(callSuper = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class ControllerException extends RuntimeException{
    /**
     * 异常状态码
     * @see ControllerErrorCodeConstants
     */
    private int exceptionCode;

    /*
    异常信息
     */
    private String exceptionMes;

    // 无参是为了序列化
    public ControllerException() {

    }

    public ControllerException(int exceptionCode, String exceptionMes) {
        this.exceptionCode = exceptionCode;
        this.exceptionMes = exceptionMes;
    }

    // 使用状态码来定义异常信息
    public ControllerException(ErrorCode errorCode) {
        this.exceptionCode = errorCode.getCode();
        this.exceptionMes = errorCode.getMsg();
    }
}
