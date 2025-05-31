package org.example.lottery_system.common.errorcode;


public interface GlobalErrorCodeConstants {
    ErrorCode SUCCESS = new ErrorCode(200, "成功");

    ErrorCode INTERNAL_Server_Error = new ErrorCode(500, "系统错误");

    ErrorCode UnKnownError = new ErrorCode(999, "未知错误");

}
