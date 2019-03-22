package com.jaagro.user.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tonyZheng
 * @date 2019-02-26 17:23
 */
@Data
@Accessors(chain = true)
public class GetTenantDto implements Serializable {
    private static final long serialVersionUID = 8254243607074981819L;
    /**
     * 租户表id
     */
    private Integer id;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司联系人
     */
    private String companyContacts;

    /**
     * 联系人手机号
     */
    private String phoneNumber;

    /**
     * 统一社会验证码
     */
    private String creditCode;

    /**
     * 所属省份
     */
    private String province;

    /**
     * 所属城市
     */
    private String city;

    /**
     * 所属区县
     */
    private String county;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 经度
     */
    private BigDecimal latitude;

    /**
     * 纬度
     */
    private BigDecimal longitude;

    /**
     * 服务开始时间
     */
    private Date startTime;

    /**
     * 服务终止时间
     */
    private Date endTime;

    /**
     * 状态(0-未审核,1-正常,2-到期,3-未审核通过,4-停止合作)
     */
    private Integer status;

    /**
     * 租户类型(待定义具体类型)
     */
    private Integer tenantType;

    /**
     * 授权个数
     */
    private Integer license;

    /**
     * 是否有效(0-无效,1-有效)
     */
    private Boolean enable;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 更新人
     */
    private Integer modifyUserId;
}
