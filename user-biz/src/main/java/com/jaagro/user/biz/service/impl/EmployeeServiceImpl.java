package com.jaagro.user.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.user.api.constant.AuditStatus;
import com.jaagro.user.api.dto.request.CreateEmpDto;
import com.jaagro.user.api.dto.request.ListEmpCriteriaDto;
import com.jaagro.user.api.dto.request.UpdateEmpDto;
import com.jaagro.user.api.dto.response.GetRoleDto;
import com.jaagro.user.api.dto.response.employee.DeleteEmployeeDto;
import com.jaagro.user.api.dto.response.employee.GetEmployeeDto;
import com.jaagro.user.api.service.*;
import com.jaagro.user.biz.config.UserIdGeneratorFactory;
import com.jaagro.user.biz.entity.*;
import com.jaagro.user.biz.mapper.*;
import com.jaagro.utils.MD5Utils;
import com.jaagro.utils.PasswordEncoder;
import com.jaagro.utils.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author baiyiran
 */
@Service
//@CacheConfig(keyGenerator = "wiselyKeyGenerator", cacheNames = "employee")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private DepartmentMapperExt departmentMapper;
    @Autowired
    private EmployeeMapperExt employeeMapper;
    @Autowired
    private BusinessSupportMapperExt businessSupportMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private VerificationCodeClientService codeClientService;
    @Autowired
    private EmployeeRoleService employeeRoleService;
    @Autowired
    private EmployeeRoleMapperExt employeeRoleMapper;
    @Autowired
    private RoleMapperExt roleMapper;
    @Autowired
    private OssSignUrlClientService ossSignUrlClientService;
    @Autowired
    private UserIdGeneratorFactory userIdGeneratorFactory;


    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    /**
     * 新增
     *
     * @param dto
     * @return
     */
//    @CacheEvict(cacheNames = "employee", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> createEmployee(CreateEmpDto dto) {
        if (StringUtils.isEmpty(dto.getPassword())) {
            throw new RuntimeException("密码不能为空");
        }
        UserInfo currentUser = userService.getCurrentUser();
        Employee employee = new Employee();
        employee.setId(userIdGeneratorFactory.getNextId());
        BeanUtils.copyProperties(dto, employee);
        Map<String, String> stringMap = PasswordEncoder.encodePassword(dto.getPassword());
        if (stringMap.size() > 0) {
            employee
                    .setTenantId(currentUser.getTenantId())
                    .setSalt(stringMap.get("salt"))
                    .setPassword(stringMap.get("password"))
                    .setCreateUserId(currentUser.getId());
            this.employeeMapper.insertSelective(employee);
            if (dto.getRoleIds() != null && dto.getRoleIds().length > 0) {
                this.employeeRoleService.createEmp(dto.getRoleIds(), employee.getId());
            }
            return ServiceResult.toResult("员工创建成功");
        } else {
            throw new RuntimeException("employee creation failed：" + employee);
        }
    }

    /**
     * 注意：不允许修改创建时间， 状态，密码、盐等字段，只允许修改基本信息
     *
     * @param dto
     * @return
     */
//    @CacheEvict(cacheNames = "employee", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> updateEmployee(UpdateEmpDto dto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);
        employee
                .setModifyUserId(userService.getCurrentUser().getId())
                .setModifyTime(new Date());
        this.employeeMapper.updateByPrimaryKeySelective(employee);
        if (dto.getRoleIds() != null && dto.getRoleIds().length > 0) {
            this.employeeRoleMapper.deleteByEmpId(dto.getId());
            for (int i = 0; i < dto.getRoleIds().length; i++) {
                Role role = this.roleMapper.selectByPrimaryKey(dto.getRoleIds()[i]);
                if (role == null) {
                    throw new NullPointerException("角色[" + dto.getRoleIds()[i] + "]不存在");
                }
                //新增
                EmployeeRole employeeRole = new EmployeeRole();
                employeeRole
                        .setCreateTime(new Date())
                        .setCreateUserId(this.userService.getCurrentUser().getId())
                        .setEmployeeId(dto.getId())
                        .setRoleId(dto.getRoleIds()[i])
                        .setEnabled(true);
                this.employeeRoleMapper.insertSelective(employeeRole);
            }
        }
        return ServiceResult.toResult("员工修改成功");
    }

    /**
     * 注意新老密码的匹配，使用common工具可以解决
     *
     * @param id
     * @param oldPassword
     * @param newPassword
     * @return
     */
//    @CacheEvict(cacheNames = "employee", allEntries = true)
    @Override
    public Map<String, Object> updatePassword(Integer id, String oldPassword, String newPassword) {
        Employee emp = this.employeeMapper.selectByPrimaryKey(id);
        if (emp == null) {
            throw new NullPointerException("员工不存在");
        }
        System.err.println(MD5Utils.encode(oldPassword, emp.getSalt()));
        System.err.println(emp.getPassword());
        if (!MD5Utils.encode(oldPassword, emp.getSalt()).equals(emp.getPassword())) {
            throw new RuntimeException("原密码不正确");
        }
        Employee employee = new Employee();
        employee
                .setId(id)
                .setModifyUserId(userService.getCurrentUser().getId())
                .setModifyTime(new Date())
                .setSalt(PasswordEncoder.encodePassword(newPassword).get("salt"))
                .setPassword(PasswordEncoder.encodePassword(newPassword).get("password"));
        return ServiceResult.toResult("员工修改密码成功");
    }

    /**
     * 重置密码
     *
     * @param phoneNumber      employee表中的手机号码字段
     * @param verificationCode 短信验证码
     * @param newPassword      新密码
     * @return
     */
    @Override
    public Map<String, Object> resetPassword(String phoneNumber, String verificationCode) {
        Boolean flag = this.codeClientService.existMessage(phoneNumber, verificationCode);
        if (!flag) {
            log.error("\n验证验证码:" + flag);
            throw new RuntimeException("验证码错误");
        }
        UserInfo userInfo = this.employeeMapper.getByPhone(phoneNumber);
        if (userInfo == null) {
            throw new RuntimeException("员工不存在");
        }
        return ServiceResult.toResult("验证成功");
    }

    @Override
    public Map<String, Object> forgetPwd(Integer id, String newPassword) {
        Employee emp = this.employeeMapper.selectByPrimaryKey(id);
        if (emp == null) {
            throw new NullPointerException("员工不存在");
        }
        Employee employee = new Employee();
        Map<String, String> result = PasswordEncoder.encodePassword(newPassword);
        employee
                .setId(id)
                .setModifyUserId(id)
                .setModifyTime(new Date())
                .setSalt(result.get("salt"))
                .setPassword(result.get("password"));
        employeeMapper.updateByPrimaryKeySelective(employee);
        return ServiceResult.toResult("员工修改密码成功");
    }

    /**
     * 注销员工
     *
     * @param id    id
     * @param notes 注销原因
     * @return
     */
//    @CacheEvict(cacheNames = "employee", allEntries = true)
    @Override
    public Map<String, Object> disableEmployee(DeleteEmployeeDto deleteEmployeeDto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(deleteEmployeeDto, employee);
        employee
                .setStatus(AuditStatus.STOP_COOPERATION)
                .setModifyUserId(userService.getCurrentUser().getId())
                .setModifyTime(new Date());
        this.employeeMapper.updateByPrimaryKeySelective(employee);
        //逻辑删除员工角色
        this.employeeRoleMapper.disableByEmpId(employee.getId());
        return ServiceResult.toResult("注销员工成功");
    }

    /**
     * 设置员工需协作部门
     *
     * @param id
     * @param deptId 需要协作的部门编号，新增到 employee_dept表中
     * @return
     */
//    @CacheEvict(cacheNames = "employee", allEntries = true)
    @Override
    public Map<String, Object> setDepartmentCooperation(Integer id, Integer[] deptId) {
        for (Integer did : deptId) {
            BusinessSupport businessSupport = new BusinessSupport();
            businessSupport
                    .setCreateUserId(userService.getCurrentUser().getId())
                    .setSupportType(1)
                    .setCreateTime(new Date())
                    .setDepartmentId(did)
                    .setEmployeeId(id)
                    .setEnabled(true);
            businessSupportMapper.insertSelective(businessSupport);
        }
        return ServiceResult.toResult("员工协作部门创建成功");
    }

    /**
     * 分页查询
     *
     * @param criteriaDto
     * @return
     */
//    @Cacheable
    @Override
    public Map<String, Object> listByCriteria(ListEmpCriteriaDto criteriaDto) {
        PageHelper.startPage(criteriaDto.getPageNum(), criteriaDto.getPageSize());
        criteriaDto.setTenantId(userService.getCurrentUser().getTenantId());
        List<Employee> emps = this.employeeMapper.listByCriteria(criteriaDto);
        return ServiceResult.toResult(new PageInfo<>(emps));
    }

    //    @Cacheable
    @Override
    public GetEmployeeDto getById(Integer id) {
        GetEmployeeDto employeeDto = this.employeeMapper.getById(id);
        if (employeeDto != null) {
            //填充部门等级
            Department department = departmentMapper.selectByPrimaryKey(employeeDto.getDepartmentId());
            if (department != null) {
                employeeDto.setLevel(department.getLevel());
            }
            if (!StringUtils.isEmpty(employeeDto.getAvatar())) {
                //替换头像地址
                String[] strArray = {employeeDto.getAvatar()};
                List<URL> urlList = ossSignUrlClientService.listSignedUrl(strArray);
                employeeDto.setAvatar(urlList.get(0).toString());
            }
            //填充员工角色列表
            List<EmployeeRole> employeeRoleList = employeeRoleMapper.listByEmpId(employeeDto.getId());
            List<GetRoleDto> roleDtoList = new ArrayList<>();
            if (employeeRoleList.size() > 0) {
                for (EmployeeRole employeeRole : employeeRoleList
                ) {
                    Role role = roleMapper.selectByPrimaryKey(employeeRole.getRoleId());
                    if (role != null) {
                        GetRoleDto getRoleDto = new GetRoleDto();
                        BeanUtils.copyProperties(role, getRoleDto);
                        roleDtoList.add(getRoleDto);
                    }
                }
                employeeDto.setRoleDtoList(roleDtoList);
            }
        }
        return employeeDto;
    }

    @Override
    public List<GetRoleDto> listRoleByEmployeeId(Integer employeeId) {
        return employeeRoleMapper.listRoleByEmployeeId(employeeId);
    }
}
