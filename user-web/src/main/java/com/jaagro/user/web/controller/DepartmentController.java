package com.jaagro.user.web.controller;

import com.jaagro.user.api.dto.request.CreateDepartmentDto;
import com.jaagro.user.api.dto.request.ListDepartmentCriteriaDto;
import com.jaagro.user.api.dto.request.UpdateDepartmentDto;
import com.jaagro.user.api.service.DepartmentService;
import com.jaagro.user.biz.entity.Department;
import com.jaagro.user.biz.mapper.DepartmentMapper;
import com.jaagro.user.biz.mapper.DepartmentMapperExt;
import com.jaagro.user.biz.mapper.EmployeeMapper;
import com.jaagro.user.biz.mapper.EmployeeMapperExt;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Service;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
@Api(value = "department", description = "部门管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DepartmentMapperExt departmentMapper;
    @Autowired
    private EmployeeMapperExt employeeMapper;

    @ApiOperation("新增部门")
    @PostMapping("/department")
    public BaseResponse insertDepartment(@RequestBody CreateDepartmentDto department) {
        if (department.getLevel() == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "level不能为空");
        }
        if (department.getLeaderEmployeeId() == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "leaderEmployeeId不能为空");
        }
        if (department.getLevel().equals(1)) {
            if (department.getParentId() != null) {
                return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "level为1时parentId不允许传值");
            }
        }
        if (!department.getLevel().equals(1)) {
            if (department.getParentId() == null) {
                return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "level不为1时parentId不能为空");
            }
            if (department.getParentId() < 1) {
                return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "parentId不正确");
            }
        }
        Map<String, Object> result;
        try {
            result = departmentService.createDepartment(department);
        } catch (Exception ex) {
            return BaseResponse.errorInstance(ex.getMessage());
        }
        return BaseResponse.service(result);
    }

    @ApiOperation("修改部门")
    @PutMapping("/department")
    public BaseResponse updateDepartment(@RequestBody UpdateDepartmentDto department) {
        if (departmentMapper.selectByPrimaryKey(department.getId()) == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "查询不到部门");
        }
        Map<String, Object> result;
        try {
            result = departmentService.updateById(department);
        } catch (Exception ex) {
            return BaseResponse.errorInstance(ex.getMessage());
        }
        return BaseResponse.service(result);
    }

    @ApiOperation("查询单个部门")
    @GetMapping("/department/{id}")
    public BaseResponse getById(@PathVariable Integer id) {
        if (this.departmentMapper.selectByPrimaryKey(id) == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "查询不到部门ID");
        }
        return BaseResponse.service(departmentService.getById(id));
    }

    @ApiOperation("删除部门[逻辑]")
    @GetMapping("/deleteById/{id}")
    public BaseResponse deleteById(@PathVariable Integer id) {
        Department department = this.departmentMapper.selectByPrimaryKey(id);
        if (department == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "查询不到相应数据");
        }
        if (department.getLevel() == 1) {
            if (departmentMapper.listByParentId(department.getId()).size() > 0) {
                return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "此部门存在下级，不得删除此部门");
            }
        }
        if (this.employeeMapper.listByDeptId(id).size() > 0) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "此部门下存在员工，不得删除此部门");
        }
        Map<String, Object> result;
        try {
            result = this.departmentService.disableDepartment(id);
        } catch (Exception ex) {
            return BaseResponse.errorInstance(ex.getMessage());
        }
        return BaseResponse.service(result);
    }

    @ApiOperation("分页查询部门")
    @PostMapping("/getByCriteria")
    public BaseResponse listByCriteria(@RequestBody ListDepartmentCriteriaDto criteriaDto) {
        if (StringUtils.isEmpty(criteriaDto.getPageNum())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageNum不能为空");
        }
        if (StringUtils.isEmpty(criteriaDto.getPageSize())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageSize不能为空");
        }
        return BaseResponse.service(this.departmentService.listByCriteria(criteriaDto));
    }

    @ApiOperation("查询部门")
    @PostMapping("/listDeparment")
    public BaseResponse listDeparment() {
        return BaseResponse.service(this.departmentService.listDepartment());
    }
}
