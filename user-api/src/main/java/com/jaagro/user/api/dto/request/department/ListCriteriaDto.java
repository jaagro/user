package com.jaagro.user.api.dto.request.department;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author byr
 */
@Data
@Accessors(chain = true)
public class ListCriteriaDto implements Serializable {

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 是否是网点
     */
    private Boolean netpoint;
}
