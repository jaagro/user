package com.jaagro.user.biz.mapper;

import com.jaagro.constant.UserInfo;
import com.jaagro.user.api.dto.request.ListEmpCriteriaDto;
import com.jaagro.user.api.dto.request.UpdateEmpDto;
import com.jaagro.user.api.dto.response.employee.GetEmployeeDto;
import com.jaagro.user.api.dto.response.employee.ListEmployeeDto;
import com.jaagro.user.biz.entity.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
public interface EmployeeMapperExt extends EmployeeMapper {

    /**
     * 登录名获取员工
     *
     * @param loginName
     * @return
     */
    UserInfo getByLoginName(String loginName);

    /**
     * 手机号获取员工
     *
     * @param phoneNumber
     * @return
     */
    UserInfo getByPhone(String phoneNumber);

    /**
     * id获取userInfo
     *
     * @param id
     * @return
     */
    UserInfo getUserInfoById(Integer id);

    /**
     * 判断用户名手机号是否已存在
     *
     * @param empDto
     * @return
     */
    Employee getByUpdateDto(UpdateEmpDto empDto);

    /**
     * 分页查询
     *
     * @param criteriaDto
     * @return
     */
    List<Employee> listByCriteria(ListEmpCriteriaDto criteriaDto);

    /**
     * 根据id查询单个
     *
     * @param id
     * @return
     */
    GetEmployeeDto getById(Integer id);

    /**
     * 根据部门id查询
     *
     * @param deptId
     * @return
     */
    List<Employee> listByDeptId(Integer deptId);

    /**
     * 获取全部员工列表
     *
     * @return
     */
    List<ListEmployeeDto> listEmployee();

    /**
     * id列表获取用户信息列表
     * @param userIdList
     * @return
     */
    List<UserInfo> listUserInfo(@Param("userIdList") List<Integer> userIdList);

    /**
     * 获取技术员列表
     * @return
     */
    List<ListEmployeeDto> listTechnician();
}