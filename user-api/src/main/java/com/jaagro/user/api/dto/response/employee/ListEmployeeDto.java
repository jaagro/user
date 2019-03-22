package com.jaagro.user.api.dto.response.employee;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author baiyiran
 */
@Data
@Accessors(chain = true)
public class ListEmployeeDto implements Serializable {
    /**
     * 员工表自增
     */
    private Integer id;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phoneNumber;
}
