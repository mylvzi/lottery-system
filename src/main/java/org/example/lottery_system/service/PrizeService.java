package org.example.lottery_system.service;

import org.example.lottery_system.controller.param.CreatePrizeParam;
import org.example.lottery_system.controller.param.PageParam;
import org.example.lottery_system.service.dto.PageListDTO;
import org.example.lottery_system.service.dto.PrizeDTO;
import org.springframework.web.multipart.MultipartFile;


public interface PrizeService {

    /**
     * 创建单个奖品
     *
     * @param param 奖品属性
     * @param picFile  上传的奖品图
     * @return  奖品id
     */
    Long createPrize(CreatePrizeParam param, MultipartFile picFile);


    /**
     * 翻页查询列表
     *
     * @param param
     * @return
     */
    PageListDTO<PrizeDTO> findPrizeList(PageParam param);
}