package com.jaagro.user.api.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author baiyiran
 */
@Data
@Accessors(chain = true)
public class ListEmpCriteriaDto {

    /**
     * 起始页
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 部门id
     */
    private Integer departmentId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 账号状态(0;注销 1；正常)
     */
    private Boolean enabled;

    /**
     * 状态
     */
    private Integer status;

}
