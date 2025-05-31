package org.example.lottery_system.service.activitystatus.operater;

import org.example.lottery_system.service.dto.ConvertActivityStatusDTO;

/**
 * 策略转换接口
 * 实现状态转换的可插拔和动态变化
 */
public abstract class AbstractActivityOperator {

    /**
     * 控制处理顺序
     * 人员+奖品-->活动
     * @return
     */
    public abstract Integer sequence();

    /**
     * 是否需要转换
     *
     * @param convertActivityStatusDTO
     * @return
     */
    public abstract Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO);

    /**
     * 转换方法
     *
     * @param convertActivityStatusDTO
     * @return
     */
    public abstract Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO);

}
