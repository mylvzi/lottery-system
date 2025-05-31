package org.example.lottery_system.service;

import org.example.lottery_system.controller.param.CreateActivityParam;
import org.example.lottery_system.controller.param.PageParam;
import org.example.lottery_system.service.dto.ActivityDTO;
import org.example.lottery_system.service.dto.ActivityDetailDTO;
import org.example.lottery_system.service.dto.CreateActivityDTO;
import org.example.lottery_system.service.dto.PageListDTO;

public interface ActivityService {

    /**
     * 创建活动
     *
     * @param param
     * @return
     */
    CreateActivityDTO createActivity(CreateActivityParam param);

    /**
     * 翻页查询活动(摘要)列表
     *
     * @param param
     * @return
     */
    PageListDTO<ActivityDTO> findActivityList(PageParam param);

    /**
     * 获取活动详细属性
     *
     * @param activityId
     * @return
     */
    ActivityDetailDTO getActivityDetail(Long activityId);

    /**
     * 缓存活动详细信息（读取表数据 再缓存）
     *
     * @param activityId
     */
    void cacheActivity(Long activityId);
}