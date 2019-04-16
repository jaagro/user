package com.jaagro.user.api.service;

import com.jaagro.user.api.dto.request.CreateDepartmentDto;
import com.jaagro.user.api.dto.request.ListDepartmentCriteriaDto;
import com.jaagro.user.api.dto.request.UpdateDepartmentDto;
import com.jaagro.user.api.dto.request.department.ListCriteriaDto;
import com.jaagro.user.api.dto.response.DepartmentReturnDto;
import com.jaagro.user.api.dto.response.department.ListDepartmentDto;

import java.util.List;
import java.util.Map;

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
     * 查询部门树
     *
     * @param listCriteriaDto
     * @return
     */
    Map<String, Object> listDepartment(ListCriteriaDto listCriteriaDto);

    /**
     * 获取下级部门的数组
     *
     * @return
     */
    List<Integer> getDownDepartment();

    /**
     * 获取指定部门id及下属部门id数组
     *
     * @return
     */
    List<Integer> getDownDepartmentByDeptId(Integer deptId);

    /**
     * 查询当前用户的本部门及本部门以下的部门
     *
     * @return
     */
    Map<String, Object> getDownDepartmentByCurrentUser();

    /**
     * 获取所有部门 供其它系统使用
     *
     * @return
     * @Author gavin 20181203
     */
    List<DepartmentReturnDto> getAllDepartments();

    /**
     * 获取网点部门
     *
     * @param netpoint
     * @return
     */
    List<ListDepartmentDto> listNetPointDepartment(ListCriteriaDto criteriaDto);

    /**
     * 获取用户所在部门
     *
     * @param userIds
     * @return
     */
    List<DepartmentReturnDto> listDepartmentByUserId(int[] userIds);

    /**
     * 获取大区id列表
     *
     * @return
     */
    List<Integer> listRegionDepartmentIds();

    /**
     * 查询指定部门的下属部门
     *
     * @param deptId
     * @return
     */
    List<Integer> getDownDeptIdsByDeptId(Integer deptId);
}
