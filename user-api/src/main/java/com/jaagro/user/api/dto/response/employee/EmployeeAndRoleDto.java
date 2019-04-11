package com.jaagro.user.api.dto.response.employee;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author gavin
 * @Date 20190410
 */
@Data
@Accessors(chain = true)
public class EmployeeAndRoleDto implements Serializable {

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
    private String phone;
    /**
     * 员工所属租户Id
     */
    private Integer tenantId;
    /**
     * 员工拥有的角色id
     */
    private Integer roleId;
    /**
     * 员工拥有的角色名称
     */
    private String roleName;
}
