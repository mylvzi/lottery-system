package org.example.lottery_system.service.activitystatus.operater;

import org.example.lottery_system.dao.dataobject.ActivityDO;
import org.example.lottery_system.dao.mapper.ActivityMapper;
import org.example.lottery_system.dao.mapper.ActivityPrizeMapper;
import org.example.lottery_system.service.dto.ConvertActivityStatusDTO;
import org.example.lottery_system.service.enums.ActivityPrizeStatusEnum;
import org.example.lottery_system.service.enums.ActivityStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityOperator extends AbstractActivityOperator {

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Override
    public Integer sequence() {
        return 2;
    }


    /**
     * 判断活动是否需要状态转换
     * @param convertActivityStatusDTO
     * @return
     */
    @Override
    public Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        Long activityId = convertActivityStatusDTO.getActivityId();
        ActivityStatusEnum targetStatus = convertActivityStatusDTO.getTargetActivityStatus();
        if (null == activityId
                || null == targetStatus) {
            return false;
        }

        ActivityDO activityDO = activityMapper.selectById(activityId);
        if (null == activityDO) {
            return false;
        }

        // 当前活动状态与传入的状态一致，不处理  传入的状态是目标状态
        if (targetStatus.name().equalsIgnoreCase(activityDO.getStatus())) {
            return false;
        }

        // 需要判断奖品是否全部抽完
        // 查询 INIT 状态的奖品个数  某个活动下包含奖品的数量  去查该状态下未被抽取的奖品个数
        int count = activityPrizeMapper.countPrize(activityId, ActivityPrizeStatusEnum.INIT.name());
        if (count > 0) {// 没有被抽完
            return false;
        }

        return true;
    }

    /**
     * 转换活动状态
     * @param convertActivityStatusDTO
     * @return
     */
    @Override
    public Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        try {// 更新数据库中的活动表的状态status字段
            activityMapper.updateStatus(convertActivityStatusDTO.getActivityId(),
                    convertActivityStatusDTO.getTargetActivityStatus().name());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
