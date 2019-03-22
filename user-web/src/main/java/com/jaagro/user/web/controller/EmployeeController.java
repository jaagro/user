package com.jaagro.user.web.controller;

import com.jaagro.constant.UserInfo;
import com.jaagro.user.api.dto.request.CheckCodeDto;
import com.jaagro.user.api.dto.request.CreateEmpDto;
import com.jaagro.user.api.dto.request.ListEmpCriteriaDto;
import com.jaagro.user.api.dto.request.UpdateEmpDto;
import com.jaagro.user.api.dto.response.GetRoleDto;
import com.jaagro.user.api.dto.response.employee.DeleteEmployeeDto;
import com.jaagro.user.api.dto.response.employee.ListEmployeeDto;
import com.jaagro.user.api.service.EmployeeRoleService;
import com.jaagro.user.api.service.EmployeeService;
import com.jaagro.user.biz.entity.Employee;
import com.jaagro.user.biz.mapper.DepartmentMapperExt;
import com.jaagro.user.biz.mapper.EmployeeMapperExt;
import com.jaagro.user.biz.mapper.EmployeeRoleMapperExt;
import com.jaagro.user.biz.mapper.RoleMapperExt;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author baiyiran
 */
@RestController
@Api(value = "employee", description = "员工管理", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeMapperExt employeeMapper;
    @Autowired
    private DepartmentMapperExt departmentMapper;
    @Autowired
    private RoleMapperExt roleMapper;
    @Autowired
    private EmployeeRoleMapperExt employeeRoleMapper;
    @Autowired
    private EmployeeRoleService employeeRoleService;

    /**
     * 新增员工
     *
     * @param createEmpDto
     * @return
     */
    @ApiOperation("新增员工")
    @PostMapping("/employee")
    public BaseResponse employee(@RequestBody CreateEmpDto createEmpDto) {
        if (StringUtils.isEmpty(createEmpDto.getName())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "员工姓名[name]不能为空");
        }
        if (StringUtils.isEmpty(createEmpDto.getLoginName())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "登录账号[loginName]不能为空");
        }
        if (StringUtils.isEmpty(createEmpDto.getPassword())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "登录密码[password]不能为空");
        }
        if (StringUtils.isEmpty(createEmpDto.getPhone())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "手机号码[phoneNumber]不能为空");
        }
        if (StringUtils.isEmpty(createEmpDto.getDepartmentId())) {
            return BaseResponse.idNull("关联部门ID[departmentId]不能为空");
        }
        if (this.departmentMapper.selectByPrimaryKey(createEmpDto.getDepartmentId()) == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "部门ID[departmentId]不存在");
        }
        //判断登陆账号和手机号是否已存在
        UpdateEmpDto updateEmpDto = new UpdateEmpDto();
        updateEmpDto
                .setLoginName(createEmpDto.getLoginName())
                .setPhone(createEmpDto.getPhone());
        if (this.employeeMapper.getByUpdateDto(updateEmpDto) != null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "登陆账号或手机号码已存在");
        }

        Map<String, Object> result;
        try {
            result = this.employeeService.createEmployee(createEmpDto);
        } catch (Exception ex) {
            return BaseResponse.errorInstance(ex.getMessage());
        }
        return BaseResponse.service(result);
    }

    /**
     * 删除
     *
     * @param id
     * @param notes
     * @return
     */
    @ApiOperation("删除员工[逻辑]")
    @PostMapping("deleteEmpById")
    public BaseResponse deleteById(@RequestBody DeleteEmployeeDto deleteEmployeeDto) {
        if (this.employeeMapper.selectByPrimaryKey(deleteEmployeeDto.getId()) == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "没有相应的员工数据");
        }
        this.employeeService.disableEmployee(deleteEmployeeDto);
        return BaseResponse.successInstance("删除员工成功");
    }

    /**
     * 修改员工基本信息
     *
     * @param updateEmpDto
     * @return
     */
    @ApiOperation("修改基本信息[包括员工角色]")
    @PutMapping("/employee")
    public BaseResponse employee(@RequestBody UpdateEmpDto updateEmpDto) {
        if (StringUtils.isEmpty(updateEmpDto.getLoginName())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "登录账号[loginName]不能为空");
        }
        if (StringUtils.isEmpty(updateEmpDto.getPhone())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "手机号码[phoneNumber]不能为空");
        }
        if (StringUtils.isEmpty(updateEmpDto.getDepartmentId())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "关联部门ID[departmentId]不能为空");
        }
        if (this.departmentMapper.selectByPrimaryKey(updateEmpDto.getDepartmentId()) == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "部门ID[departmentId]不存在");
        }
        //判断登陆账号和手机号是否已存在
        if (this.employeeMapper.getByUpdateDto(updateEmpDto) != null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "登陆账号或手机号码已存在");
        }
        Map<String, Object> result = new HashMap<>();
        try {
            result = this.employeeService.updateEmployee(updateEmpDto);
        } catch (Exception ex) {
            BaseResponse.errorInstance(ex.getMessage());
        }
        return BaseResponse.service(result);
    }

    /**
     * 修改员工密码
     *
     * @param oldPassword
     * @param newPassword
     * @param id
     * @return
     */
    @ApiOperation("修改密码")
    @PostMapping("/updateEmpPassword")
    public BaseResponse updateEmpPassword(@RequestBody CheckCodeDto checkCodeDto) {
        if (StringUtils.isEmpty(checkCodeDto.getPhone())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "电话能不为空");
        }
        UserInfo employee = this.employeeMapper.getByPhone(checkCodeDto.getPhone());
        if (employee == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "此员工不存在:" + checkCodeDto.getPhone());
        }
        if (StringUtils.isEmpty(checkCodeDto.getNewPassword())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "新密码不能为空");
        }
        if (StringUtils.isEmpty(checkCodeDto.getOldPassword())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "旧密码不能为空");
        }
        try {
            this.employeeService.updatePassword(employee.getId(), checkCodeDto.getOldPassword(), checkCodeDto.getNewPassword());
        } catch (Exception e) {
            return BaseResponse.errorInstance(e.getMessage());
        }
        return BaseResponse.successInstance("员工修改密码成功");
    }

    /**
     * 校验验证码
     *
     * @param checkCodeDto
     * @return
     */
    @ApiOperation("校验验证码")
    @PostMapping("/checkCode")
    public BaseResponse resetPassword(@RequestBody CheckCodeDto checkCodeDto) {
        if (StringUtils.isEmpty(checkCodeDto.getPhone())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "电话不能为空");
        }
        if (StringUtils.isEmpty(checkCodeDto.getVerificationCode())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "验证码不能为空");
        }
        UserInfo userInfo = this.employeeMapper.getByPhone(checkCodeDto.getPhone());
        if (userInfo == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "此员工不存在:phone:" + checkCodeDto.getPhone());
        }
        try {
            this.employeeService.resetPassword(checkCodeDto.getPhone(), checkCodeDto.getVerificationCode());
        } catch (RuntimeException e) {
            return BaseResponse.errorInstance(e.getMessage());
        }
        return BaseResponse.successInstance("重置密码成功");
    }

    /**
     * 忘记密码
     *
     * @param checkCodeDto
     * @return
     */
    @ApiOperation("忘记密码")
    @PostMapping("/forgetPassword")
    public BaseResponse forgetPassword(@RequestBody CheckCodeDto checkCodeDto) {
        if (StringUtils.isEmpty(checkCodeDto.getPhone())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "电话不能为空");
        }
        if (StringUtils.isEmpty(checkCodeDto.getNewPassword())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "新密码不能为空");
        }
        UserInfo userInfo = this.employeeMapper.getByPhone(checkCodeDto.getPhone());
        if (userInfo == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "此员工不存在:phone:" + checkCodeDto.getPhone());
        }
        try {
            this.employeeService.forgetPwd(userInfo.getId(), checkCodeDto.getNewPassword());
        } catch (RuntimeException e) {
            return BaseResponse.errorInstance(e.getMessage());
        }
        return BaseResponse.successInstance("重置密码成功");
    }

    /**
     * 创建员工需协作部门
     *
     * @param departmentIds
     * @param id
     * @return
     */
    @ApiOperation("创建员工需协作部门")
    @PostMapping("/createEmpDepartment")
    public BaseResponse createEmpDepartment(@RequestParam(value = "departmentIds") Integer[] departmentIds, @RequestParam(value = "id") Integer id) {
        if (this.employeeMapper.selectByPrimaryKey(id) == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "员工[id:" + id + "]不存在");
        }
        if (departmentIds.length > 0) {
            for (Integer did : departmentIds) {
                if (this.departmentMapper.selectByPrimaryKey(did) == null) {
                    return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "部门[id:" + did + "]不存在");
                }
            }
        } else {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "部门ids不能为空");
        }
        this.employeeService.setDepartmentCooperation(id, departmentIds);
        return BaseResponse.successInstance("员工协作部门创建成功");
    }

    /**
     * 单个查询
     *
     * @param id
     * @return
     */
    @ApiOperation("查询单个员工")
    @GetMapping("/getEmpById/{id}")
    public BaseResponse getEmp(@PathVariable Integer id) {
        if (this.employeeMapper.selectByPrimaryKey(id) == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "员工不存在");
        }
        return BaseResponse.successInstance(this.employeeService.getById(id));
    }

    /**
     * 根据部门id查询员工列表
     *
     * @param
     * @return
     */
    @ApiOperation("根据部门id查询员工列表")
    @GetMapping("/listEmpByDeptId/{deptId}")
    public BaseResponse<List<Employee>> getEmpByDeptId(@PathVariable Integer deptId) {
        if (this.departmentMapper.selectByPrimaryKey(deptId) == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "部门不存在");
        }
        return BaseResponse.successInstance(this.employeeMapper.listByDeptId(deptId));
    }

    /**
     * 分页查询
     *
     * @param criteriaDto
     * @return
     */
    @ApiOperation("分页查询员工")
    @PostMapping("/listEmpByCriteria")
    public BaseResponse listEmpByCriteria(@RequestBody ListEmpCriteriaDto criteriaDto) {
        return BaseResponse.service(this.employeeService.listByCriteria(criteriaDto));
    }

    /**
     * 根据部门id查询员工列表
     *
     * @param
     * @return
     */
    @ApiOperation("获取全部员工列表")
    @GetMapping("/listEmployee")
    public BaseResponse listEmployee() {
        return BaseResponse.successInstance(this.employeeMapper.listEmployee());
    }

    @GetMapping("/listRoleByEmployeeId/{employeeId}")
    public BaseResponse<List<GetRoleDto>> listRoleByEmployeeId(@PathVariable("employeeId") Integer employeeId) {
        List<GetRoleDto> getRoleDtoList = employeeService.listRoleByEmployeeId(employeeId);
        log.info("O listRoleByEmployeeId employeeId={},getRoleDtoList={}", employeeId, getRoleDtoList);
        if (CollectionUtils.isEmpty(getRoleDtoList)) {
            return BaseResponse.queryDataEmpty();
        }
        return BaseResponse.successInstance(getRoleDtoList);
    }

    /**
     * 获取技术员列表
     *
     * @return
     * @author yj
     */
    @ApiOperation("获取技术员列表")
    @GetMapping("/listTechnician")
    public BaseResponse<List<ListEmployeeDto>> listTechnician() {
        List<ListEmployeeDto> listEmployeeDtoList = employeeMapper.listTechnician();
        if (CollectionUtils.isEmpty(listEmployeeDtoList)) {
            return BaseResponse.queryDataEmpty();
        }
        return BaseResponse.successInstance(listEmployeeDtoList);
    }
}
