package com.jaagro.user.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Driver {
    /**
     * 主键司机ID
     */
    private Integer id;

    /**
     * 
     */
    private Integer tenantId;

    /**
     * 关联车队表ID
     */
    private Integer truckTeamId;

    /**
     * 关联车辆ID
     */
    private Integer truckId;

    /**
     * 司机姓名
     */
    private String name;

    /**
     * 身份证号码
     */
    private String identityCard;

    /**
     * 驾照类型
 (1=A1、2=A2、3=B1、4=B2、5=C1、6=C2)
     */
    private Integer drivingLicense;

    /**
     * 是否是主驾驶员(0 不是 1 是)
     */
    private Boolean maindriver;

    /**
     * 登录账号
     */
    private String loginName;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 密码加盐
     */
    private String salt;

    /**
     * 驾驶证到期时间
     */
    private String expiryDrivingLicense;

    /**
     * 驾驶员帐号状态
     */
    private Integer status;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 修改日期
     */
    private Date modifyTime;

    /**
     * 创建人(关联用户ID)
     */
    private Integer createUserId;

    /**
     * 修改人(关联用户ID)
     */
    private Integer modifyUserId;

    /**
     * 备注
     */
    private String notes;

    /**
     * 所属城市
     */
    private String city;

    /**
     * 所属省份
     */
    private String province;

    /**
     * 所属区县
     */
    private String county;

    /**
     * 设备标识，用于jpush
     */
    private String registrationId;
}