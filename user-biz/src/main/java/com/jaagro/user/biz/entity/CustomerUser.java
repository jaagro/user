package com.jaagro.user.biz.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class CustomerUser {
    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 
     */
    private Integer tenantId;

    /**
     * 客户类型: 1-签约客户 2-装货地客户 3-卸货地客户
     */
    private Integer customerType;

    /**
     * 所属客户(关联客户表)
     */
    private Integer relevanceId;

    /**
     * 登录用户名(默认使用手机号码)
     */
    private String loginName;

    /**
     * 登录密码(加盐,加密)
     */
    private String password;

    /**
     * 加盐值
     */
    private String salt;

    /**
     * 帐号状态(0；未审核  1；审核未通过 2－停止合作，3－正常合作)
     */
    private Integer status;

    /**
     * 显示名称
     */
    private String name;

    /**
     * 用户的头像(存储头像的路径)
     */
    private String avatar;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 修改人
     */
    private Integer modifyUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;
}