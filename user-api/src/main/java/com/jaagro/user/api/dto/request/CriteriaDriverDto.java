package com.jaagro.user.api.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author baiyiran
 * @date 2019-04-16
 */
@Data
@Accessors(chain = true)
public class CriteriaDriverDto implements Serializable {
    /**
     * 电话
     */
    private String phoneNumber;

    /**
     * 租户id
     */
    private Integer tenantId;

}