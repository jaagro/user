package com.jaagro.user.api.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @author baiyiran
 * @date 2019.4.8
 */
@Data
@Accessors(chain = true)
public class UpdateCustomerUserDto implements Serializable {

    /**
     * 备用id，当relevance_id不够使用时用于扩展字段
     */
    private Integer standbyId;

    /**
     * 登录用户名(默认使用手机号码)
     */
    private String loginName;

    /**
     * 显示名称
     */
    private String name;

    /**
     * 手机号码
     */
    private String phoneNumber;

}
