package org.example.lottery_system.service.dto;

import lombok.Data;
import org.example.lottery_system.service.enums.ActivityPrizeStatusEnum;
import org.example.lottery_system.service.enums.ActivityStatusEnum;
import org.example.lottery_system.service.enums.ActivityUserStatusEnum;

import java.util.List;

@Data
public class ConvertActivityStatusDTO {

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动目标状态
     */
    private ActivityStatusEnum targetActivityStatus;

    /**
     * 奖品id
     */
    private Long prizeId;

    /**
     * 奖品目标状态
     */
    private ActivityPrizeStatusEnum targetPrizeStatus;

    /**
     * 人员id列表
     */
    private List<Long> userIds;

    /**
     * 人员目标状态
     */
    private ActivityUserStatusEnum targetUserStatus;

}
