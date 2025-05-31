package org.example.lottery_system.service.activitystatus.impl;

import org.example.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.example.lottery_system.common.exception.ServiceException;
import org.example.lottery_system.service.ActivityService;
import org.example.lottery_system.service.activitystatus.ActivityStatusManager;
import org.example.lottery_system.service.activitystatus.operater.AbstractActivityOperator;
import org.example.lottery_system.service.dto.ConvertActivityStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class ActivityStatusManagerImpl implements ActivityStatusManager {

    private static final Logger logger = LoggerFactory.getLogger(ActivityStatusManagerImpl.class);


    // key -->对应的策略
    @Autowired
    private final Map<String, AbstractActivityOperator> operatorMap = new HashMap<>();
    @Autowired
    private ActivityService activityService;

    /**
     * 处理状态转换的具体实现
     * @param convertActivityStatusDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlerEvent(ConvertActivityStatusDTO convertActivityStatusDTO) {
        // 1、活动状态扭转有依赖性，导致代码维护性差
        // 2、状态扭转条件可能会扩展，当前写法，扩展性差，维护性差

        if (CollectionUtils.isEmpty(operatorMap)) {
            logger.warn("operatorMap 为空！");
            return;
        }

        // 此处封装进map-->工厂模式
        Map<String, AbstractActivityOperator> currMap = new HashMap<>(operatorMap);
        Boolean update = false;

        // 先处理：人员、奖品
        update = processConvertStatus(convertActivityStatusDTO, currMap, 1);

        // 后处理：活动
        update = processConvertStatus(convertActivityStatusDTO, currMap, 2) || update;

        // 更新缓存  必须是上述状态扭转成功
        if (update) {
            activityService.cacheActivity(convertActivityStatusDTO.getActivityId());
        }

    }



    @Override
    public void rollbackHandlerEvent(ConvertActivityStatusDTO convertActivityStatusDTO) {
        // operatorMap：活动、奖品、人员  此处没有先后的执行顺序
        // 活动是否需要回滚？？ 绝对需要，
        // 原因：奖品都恢复成INIT，那么这个活动下的奖品绝对没抽完
        // 回滚：将状态转换为init
        for (AbstractActivityOperator operator : operatorMap.values()) {
            operator.convert(convertActivityStatusDTO);
        }

        // 缓存更新
        activityService.cacheActivity(convertActivityStatusDTO.getActivityId());
    }

    /**
     * 扭转状态
     *
     * @param convertActivityStatusDTO  传入的转换状态参数(controller层)
     * @param currMap
     * @param sequence  责任链模式中确定的执行顺序
     * @return
     */
    private Boolean processConvertStatus(ConvertActivityStatusDTO convertActivityStatusDTO,
                                         Map<String, AbstractActivityOperator> currMap,
                                         int sequence) {
        Boolean update = false;

        // 遍历currMap
        Iterator<Map.Entry<String, AbstractActivityOperator>> iterator = currMap.entrySet().iterator();
        while (iterator.hasNext()) {
            AbstractActivityOperator operator = iterator.next().getValue();
            // Operator 是否需要转换
            if (operator.sequence() != sequence
                    || !operator.needConvert(convertActivityStatusDTO)) {
                continue;
            }

            // 需要转换：转换  执行各自的转换方法
            if (!operator.convert(convertActivityStatusDTO)) {
                logger.error("{}状态转换失败！", operator.getClass().getName());
                throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_STATUS_CONVERT_ERROR);
            }

            // currMap 删除当前 Operator
            iterator.remove();
            update = true;
        }

        // 返回
        return update;
    }
}