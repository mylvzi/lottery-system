package org.example.lottery_system.service.dto;

import lombok.Data;
import org.example.lottery_system.service.enums.ActivityPrizeTiersEnum;

import java.util.Date;

@Data
public class WinningRecordDTO {
    /**
     * 中奖者id
     */
    private Long winnerId;

    /**
     * 中奖者姓名
     */
    private String winnerName;

    /**
     * 奖品名
     */
    private String prizeName;

    /**
     * 等级
     */
    private ActivityPrizeTiersEnum prizeTier;

    /**
     * 中奖时间
     */
    private Date winningTime;

}