package org.example.lottery_system.service.dto;

import lombok.Data;

import java.util.List;

/**
 * @author: yibo
 */
@Data
public class PageListDTO<T> {

    /**
     * 总量
     */
    private Integer total;

    /**
     * 当前页列表
     */
    private List<T> records;


    public PageListDTO(Integer total, List<T> records) {
        this.total = total;
        this.records = records;
    }


}
