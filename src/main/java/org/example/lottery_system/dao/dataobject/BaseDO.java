package org.example.lottery_system.dao.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 所有表对象公用的属性
 */
@Data
public class BaseDO implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;


}