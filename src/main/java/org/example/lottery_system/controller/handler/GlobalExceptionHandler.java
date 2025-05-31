package org.example.lottery_system.controller.handler;

import org.example.lottery_system.common.errorcode.GlobalErrorCodeConstants;
import org.example.lottery_system.common.exception.ControllerException;
import org.example.lottery_system.common.exception.ServiceException;
import org.example.lottery_system.common.pojo.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(value = ServiceException.class)
    public CommonResult<?> serviceException(ServiceException e) {
        // 打错误日志
        logger.error("serviceException:", e);
        // 构造错误结果
        return CommonResult.error(
                GlobalErrorCodeConstants.INTERNAL_Server_Error.getCode(),
                e.getMessage());
    }

    @ExceptionHandler(value = ControllerException.class)
    public CommonResult<?> controllerException(ControllerException e) {
        // 打错误日志
        logger.error("controllerException:", e);
        // 构造错误结果
        return CommonResult.error(
                GlobalErrorCodeConstants.INTERNAL_Server_Error.getCode(),
                e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public CommonResult<?> exception(Exception e) {
        // 打错误日志
        logger.error("服务异常:", e);
        // 构造错误结果
        return CommonResult.error(
                GlobalErrorCodeConstants.INTERNAL_Server_Error.getCode(),
                e.getMessage());
    }


}