package com.jaagro.user.api.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liqiangping
 */
@Data
@Accessors(chain = true)
public class ListDepartmentCriteriaDto implements Serializable {

    /**
     * 起始页
     */
    private Integer pageNum;


    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

}
