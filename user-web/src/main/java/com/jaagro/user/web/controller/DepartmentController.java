package com.jaagro.user.web.controller;

import com.jaagro.user.api.dto.request.CreateDepartmentDto;
import com.jaagro.user.api.dto.request.ListDepartmentCriteriaDto;
import com.jaagro.user.api.dto.request.UpdateDepartmentDto;
import com.jaagro.user.api.dto.response.DepartmentReturnDto;
import com.jaagro.user.api.dto.response.department.ListDepartmentDto;
import com.jaagro.user.api.service.DepartmentService;
import com.jaagro.user.biz.entity.Department;
import com.jaagro.user.biz.mapper.DepartmentMapperExt;
import com.jaagro.user.biz.mapper.EmployeeMapperExt;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "部门级别不能为空");
        }
        if (department.getLevel().equals(1)) {
            if (department.getParentId() != null) {
                return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "部门级别为一级时父级部门不允许传值");
            }
        }
        if (!department.getLevel().equals(1)) {
            if (department.getParentId() == null) {
                return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "部门级别不为一级时父级部门不能为空");
            }
            if (department.getParentId() < 1) {
                return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "父级部门不正确");
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
        if (department.getLevel() != null) {
            if (department.getLevel().equals(1)) {
                department.setParentId(null);
            }
            if (!department.getLevel().equals(1)) {
                if (department.getParentId() == null) {
                    return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "部门级别不为一级时父级部门不能为空");
                }
                if (department.getParentId() < 1) {
                    return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "父级部门不正确");
                }
            }
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
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "查询不到部门");
        }
        return BaseResponse.successInstance(departmentService.getById(id));
    }

    /**
     * @param id
     * @return
     * @Author gavin
     */
    @ApiOperation("查询单个部门")
    @GetMapping("/getDepartmentById/{id}")
    public DepartmentReturnDto getDepartmentById(@PathVariable Integer id) {
        return departmentService.getById(id);
    }

    @Ignore
    @ApiOperation("查询单个部门名称提供给crm")
    @GetMapping("/getDeptNameById/{id}")
    public String getDeptNameById(@PathVariable Integer id) {
        if (this.departmentMapper.selectByPrimaryKey(id) == null) {
            return null;
        }
        return this.departmentMapper.selectByPrimaryKey(id).getDepartmentName();
    }

    @ApiOperation("删除部门[逻辑]")
    @GetMapping("/deleteById/{id}")
    public BaseResponse deleteById(@PathVariable Integer id) {
        Department department = this.departmentMapper.selectByPrimaryKey(id);
        if (department == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "查询不到相应数据");
        }
        if (departmentMapper.listByParentId(department.getId()).size() > 0) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "此部门存在下级，不得删除此部门");
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
    public BaseResponse listDepartment(@RequestParam(required = false) Boolean netpoint) {
        return BaseResponse.service(this.departmentService.listDepartment(netpoint));
    }

    @ApiOperation("查询网点部门")
    @PostMapping("/listNetPointDepartment")
    public List<Map<String, String>> listNetPointDepartment(@RequestParam(required = false) Boolean netpoint) {
        List<Map<String, String>> mapList = new ArrayList<>();
        List<ListDepartmentDto> deptDtos = departmentService.listNetPointDepartment(netpoint);
        for (ListDepartmentDto deptDto : deptDtos) {
            Map<String, String> map = new HashMap<>();
            map.put("id", deptDto.getId().toString());
            map.put("departmentName", deptDto.getDepartmentName());
            mapList.add(map);
        }

        return mapList;
    }

    @PostMapping("/getDownDepartment")
    public List<Integer> getDownDepartment() {
        return departmentService.getDownDepartment();
    }

    /**
     * 获取指定部门id及下属部门
     *
     * @return
     */
    @ApiOperation("获取指定部门id及下属部门id数组")
    @PostMapping("/getDownDepartmentByDeptId/{deptId}")
    public List<Integer> getDownDepartmentByDeptId(@PathVariable("deptId") Integer deptId) {
        return departmentService.getDownDepartmentByDeptId(deptId);
    }

    /**
     * 查询当前用户的本部门及本部门以下的部门
     *
     * @returns
     */
    @ApiOperation("查询当前用户下属部门及其本身部门树")
    @PostMapping("/getDownDepartmentByCurrentUser")
    public BaseResponse<Map<String, Object>> getDownDepartmentByCurrentUser() {
        return BaseResponse.service(departmentService.getDownDepartmentByCurrentUser());
    }

    /**
     * 获取所有部门 供其它系统使用
     *
     * @return
     * @Author gavin 20181203
     */
    @ApiOperation("查询所有部门")
    @PostMapping("/getAllDepartments")
    public List<DepartmentReturnDto> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    /**
     * 批量获取部门
     *
     * @param ids
     * @return
     */
    public BaseResponse<List<DepartmentReturnDto>> listDepartmentByIdList(int[] ids) {
        return BaseResponse.successInstance(departmentService.listDepartmentByUserId(ids));
    }

    /**
     * 获取全部大区id
     *
     * @return
     * @Author byr
     */
    @ApiOperation("获取全部大区id")
    @GetMapping("/listRegionDepartmentIds")
    @Ignore
    public List<Integer> listRegionDepartmentIds() {
        return departmentService.listRegionDepartmentIds();
    }
}
