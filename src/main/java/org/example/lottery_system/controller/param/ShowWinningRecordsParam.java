package org.example.lottery_system.controller.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class ShowWinningRecordsParam implements Serializable {

    /**
     * 活动id
     */
    @NotNull(message = "活动id不能为空！")
    private Long activityId;

    /**
     * 奖品id
     */
    private Long prizeId;
}
