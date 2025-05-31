package org.example.lottery_system.controller;

import org.example.lottery_system.common.errorcode.ControllerErrorCodeConstants;
import org.example.lottery_system.common.exception.ControllerException;
import org.example.lottery_system.common.pojo.CommonResult;
import org.example.lottery_system.common.utils.JacksonUtil;
import org.example.lottery_system.controller.param.CreateActivityParam;
import org.example.lottery_system.controller.param.PageParam;
import org.example.lottery_system.controller.result.CreateActivityResult;
import org.example.lottery_system.controller.result.FindActivityListResult;
import org.example.lottery_system.controller.result.GetActivityDetailResult;
import org.example.lottery_system.service.ActivityService;
import org.example.lottery_system.service.dto.ActivityDTO;
import org.example.lottery_system.service.dto.ActivityDetailDTO;
import org.example.lottery_system.service.dto.CreateActivityDTO;
import org.example.lottery_system.service.dto.PageListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
public class ActivityController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityService activityService;

    // user 45 44
    // prize 18 19

    /**
     * 创建活动
     *
     * @param param
     * @return
     */
    @RequestMapping("/activity/create")
    public CommonResult<CreateActivityResult> createActivity(
            @Validated @RequestBody CreateActivityParam param) {
        logger.info("createActivity CreateActivityParam:{}",
                JacksonUtil.writeValueAsString(param));

        return CommonResult.success(
                convertToCreateActivityResult(activityService.createActivity(param)));
    }

    @RequestMapping("/activity/find-list")
    public CommonResult<FindActivityListResult> findActivityList(PageParam param) {
        logger.info("findActivityList PageParam:{}",
                JacksonUtil.writeValueAsString(param));
        return CommonResult.success(
                convertToFindActivityListResult(
                        activityService.findActivityList(param)));
    }

    @RequestMapping("/activity-detail/find")
    public CommonResult<GetActivityDetailResult> getActivityDetail(Long activityId) {
        logger.info("getActivityDetail activityId:{}", activityId);
        ActivityDetailDTO detailDTO = activityService.getActivityDetail(activityId);
        return CommonResult.success(convertToGetActivityDetailResult(detailDTO));
    }

    private GetActivityDetailResult convertToGetActivityDetailResult(ActivityDetailDTO detailDTO) {
        if (null == detailDTO) {
            throw new ControllerException(ControllerErrorCodeConstants.GET_ACTIVITY_DETAIL_ERROR);
        }

        GetActivityDetailResult result = new GetActivityDetailResult();
        result.setActivityId(detailDTO.getActivityId());
        result.setActivityName(detailDTO.getActivityName());
        result.setDescription(detailDTO.getDesc());
        result.setValid(detailDTO.valid());

        // 抽奖顺序：一等奖、二、三  需要在前端进行展示
        // 首先根据奖品列表的等级进行排序
        result.setPrizes(
                detailDTO.getPrizeDTOList().stream()
                        .sorted(Comparator.comparingInt(prizeDTO -> prizeDTO.getTiers().getCode()))
                        .map(prizeDTO -> {
                            GetActivityDetailResult.Prize prize = new GetActivityDetailResult.Prize();
                            prize.setPrizeId(prizeDTO.getPrizeId());
                            prize.setName(prizeDTO.getName());
                            prize.setImageUrl(prizeDTO.getImageUrl());
                            prize.setPrice(prizeDTO.getPrice());
                            prize.setDescription(prizeDTO.getDescription());
                            prize.setPrizeTierName(prizeDTO.getTiers().getMessage());
                            prize.setPrizeAmount(prizeDTO.getPrizeAmount());
                            prize.setValid(prizeDTO.valid());
                            return prize;
                        }).collect(Collectors.toList())
        );

        result.setUsers(
                detailDTO.getUserDTOList().stream()
                        .map(userDTO -> {
                            GetActivityDetailResult.User user = new GetActivityDetailResult.User();
                            user.setUserId(userDTO.getUserId());
                            user.setUserName(userDTO.getUserName());
                            user.setValid(userDTO.valid());
                            return user;
                        }).collect(Collectors.toList())
        );
        return result;
    }

    private FindActivityListResult convertToFindActivityListResult(PageListDTO<ActivityDTO> activityList) {
        if (null == activityList) {
            throw new ControllerException(ControllerErrorCodeConstants.FIND_ACTIVITY_LIST_ERROR);
        }
        FindActivityListResult result = new FindActivityListResult();
        result.setTotal(activityList.getTotal());
        result.setRecords(
                activityList.getRecords()
                        .stream()
                        .map(activityDTO -> {
                            FindActivityListResult.ActivityInfo activityInfo = new FindActivityListResult.ActivityInfo();
                            activityInfo.setActivityId(activityDTO.getActivityId());
                            activityInfo.setActivityName(activityDTO.getActivityName());
                            activityInfo.setDescription(activityDTO.getDescription());
                            activityInfo.setValid(activityDTO.valid());
                            return activityInfo;
                        }).collect(Collectors.toList())
        );
        return result;
    }

    private CreateActivityResult convertToCreateActivityResult(CreateActivityDTO createActivityDTO) {
        if (null == createActivityDTO) {
            throw new ControllerException(ControllerErrorCodeConstants.CREATE_ACTIVITY_ERROR);
        }
        CreateActivityResult result = new CreateActivityResult();
        result.setActivityId(createActivityDTO.getActivityId());
        return result;
    }


}