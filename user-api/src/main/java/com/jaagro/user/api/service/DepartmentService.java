package com.jaagro.user.api.service;

import com.jaagro.user.api.dto.request.CreateDepartmentDto;
import com.jaagro.user.api.dto.request.ListDepartmentCriteriaDto;
import com.jaagro.user.api.dto.request.UpdateDepartmentDto;
import com.jaagro.user.api.dto.response.DepartmentReturnDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author tony
 */
public interface DepartmentService {
    /**
     * 创建部门
     *
     * @param dto
     * @return
     */
    Map<String, Object> createDepartment(CreateDepartmentDto dto);

    /**
     * 修改部门
     *
     * @param dto
     * @return
     */
    Map<String, Object> updateById(UpdateDepartmentDto dto);

    /**
     * 获取单条
     *
     * @param id
     * @return
     */
    DepartmentReturnDto getById(Integer id);

    /**
     * 逻辑删除部门
     *
     * @param id
     * @return
     */
    Map<String, Object> disableDepartment(Integer id);

    /**
     * 分页获取list，注意criteria查询条件
     *
     * @param dto
     * @return
     */
    Map<String, Object> listByCriteria(ListDepartmentCriteriaDto dto);

    /**
     * 查询部门列表树
     *
     * @return
     */
    Map<String, Object> listDepartment(Boolean netpoint);

    /**
     * 获取下级部门的数组
     *
     * @return
     */
    List<Integer> getDownDepartment();
}
