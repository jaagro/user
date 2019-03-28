package com.jaagro.user.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.user.api.dto.request.CreateDepartmentDto;
import com.jaagro.user.api.dto.request.ListDepartmentCriteriaDto;
import com.jaagro.user.api.dto.request.UpdateDepartmentDto;
import com.jaagro.user.api.dto.response.DepartmentReturnDto;
import com.jaagro.user.api.dto.response.department.ListDepartmentDto;
import com.jaagro.user.api.service.AuthClientService;
import com.jaagro.user.api.service.DepartmentService;
import com.jaagro.user.api.service.UserService;
import com.jaagro.user.biz.entity.BusinessSupport;
import com.jaagro.user.biz.entity.Department;
import com.jaagro.user.biz.mapper.BusinessSupportMapperExt;
import com.jaagro.user.biz.mapper.DepartmentMapperExt;
import com.jaagro.user.biz.mapper.EmployeeMapperExt;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author tony
 */
@Service
//@CacheConfig(keyGenerator = "wiselyKeyGenerator", cacheNames = "department")
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    @Autowired
    private DepartmentMapperExt departmentMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeMapperExt employeeMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AuthClientService authClientService;
    @Autowired
    private BusinessSupportMapperExt businessSupportMapper;

    /**
     * 创建部门
     *
     * @param dto
     * @return
     */
//    @CacheEvict(cacheNames = "department", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> createDepartment(CreateDepartmentDto dto) {
        if (dto.getParentId() != null) {
            if (departmentMapper.selectByPrimaryKey(dto.getParentId()) == null) {
                return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "上级部门不存在");
            }
        }
        if (dto.getLeaderEmployeeId() != null) {
            if (employeeMapper.selectByPrimaryKey(dto.getLeaderEmployeeId()) == null) {
                return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "主管不存在");
            }
        }
        //创建部门对象
        Department department = new Department();
        BeanUtils.copyProperties(dto, department);
        department
                .setCreateUserId(userService.getCurrentUser().getId());
        departmentMapper.insertSelective(department);
        return ServiceResult.toResult("部门创建成功");
    }

    /**
     * 修改部门
     *
     * @param dto
     * @return
     */
//    @CacheEvict(cacheNames = "department", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> updateById(UpdateDepartmentDto dto) {
        //创建部门对象
        Department department = new Department();
        BeanUtils.copyProperties(dto, department);
        department
                .setModifyTime(new Date())
                .setModifyUserId(userService.getCurrentUser().getId());
        departmentMapper.updateByPrimaryKeySelective(department);
        if (dto.getLevel() != null) {
            if (dto.getLevel().equals(1)) {
                if (dto.getParentId() == null) {
                    Department department1 = departmentMapper.selectByPrimaryKey(department.getId());
                    department1.setParentId(null);
                    departmentMapper.updateByPrimaryKey(department1);
                }
            }
        }
        return ServiceResult.toResult("修改部门成功");
    }

    /**
     * 查询单个部门
     *
     * @param id
     * @return
     */
//    @Cacheable
    @Override
    public DepartmentReturnDto getById(Integer id) {
        if (departmentMapper.selectByPrimaryKey(id) == null) {
            throw new NullPointerException("id: " + id + "不存在");
        }
        DepartmentReturnDto departmentReturnDto = departmentMapper.getById(id);
        if (departmentReturnDto != null) {
            Department department = departmentMapper.selectByPrimaryKey(departmentReturnDto.getParentId());
            if (department != null) {
                departmentReturnDto.setParentLevel(department.getLevel());
            }
        }
        return departmentReturnDto;
    }

    /**
     * 逻辑删除部门
     *
     * @param id
     * @return
     */
//    @CacheEvict(cacheNames = "department", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> disableDepartment(Integer id) {
        //创建部门Dto返回的对象
        Department department = departmentMapper.selectByPrimaryKey(id);
        if (department == null) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "部门查询无数据");
        }
        //逻辑删除部门
        department.setEnabled(false);
        departmentMapper.updateByPrimaryKeySelective(department);
        return ServiceResult.toResult("部门删除成功");
    }

    /**
     * 分页获取list，注意criteria查询条件
     *
     * @param dto
     * @return
     */
//    @Cacheable
    @Override
    public Map<String, Object> listByCriteria(ListDepartmentCriteriaDto dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<DepartmentReturnDto> departmentReturnDtos = this.departmentMapper.getByCriteriDto(dto);
        return ServiceResult.toResult(new PageInfo<>(departmentReturnDtos));
    }

    //    @Cacheable
    @Override
    public Map<String, Object> listDepartment(Boolean netpoint) {
        return ServiceResult.toResult(this.departmentMapper.listDepartment(netpoint));
    }

    /**
     * 获取网点部门
     *
     * @param netpoint
     * @return
     */
    @Override
    public List<ListDepartmentDto> listNetPointDepartment(Boolean netpoint) {
        return departmentMapper.listNetPointDepartment(netpoint);
    }

    /**
     * 获取用户所在部门
     *
     * @param userIds
     * @return
     */
    @Override
    public List<DepartmentReturnDto> listDepartmentByUserId(int[] userIds) {

//        int[] deptIds = new int[1];
//        if (deptIds.length > 0) {
//            StringBuilder stringBuilder = new StringBuilder();
//            for (int i = 0; i < deptIds.length; i++) {
//                if (i == deptIds.length - 1) {
//                    stringBuilder = stringBuilder.append(deptIds[i]);
//                }
//                stringBuilder = stringBuilder.append(deptIds[i]).append(",");
//            }
//            String deptIdStr = stringBuilder.toString();
//            System.out.println(deptIdStr);
//            return departmentMapper.listDepartmentByIdList(deptIdStr);
//        }
//        log.info("O listDepartmentByUserId: ");
        return null;
    }

    /**
     * 获取大区id列表
     *
     * @return
     */
    @Override
    public List<Integer> listRegionDepartmentIds() {
        return departmentMapper.listRegionDepartmentIds();
    }

    /**
     * 查询指定部门的下属部门
     *
     * @param deptId
     * @return
     */
    @Override
    public List<Integer> getDownDeptIdsByDeptId(Integer deptId) {
        Set<Integer> deptIdSet = new LinkedHashSet<>();
        Set<Integer> set = departmentRecursion2(deptIdSet, deptId);
        if (CollectionUtils.isEmpty(set)) {
            return null;
        } else {
            return new ArrayList<>(set);
        }
    }

    /**
     * 获取下级部门的数组
     * 用于系统的数据隔离使用
     *
     * @return
     */
//    @Cacheable
    @Override
    public List<Integer> getDownDepartment() {
        String token = request.getHeader("token");
        UserInfo userInfo = authClientService.getUserByToken(token);
        //当前user专管列表
        List<BusinessSupport> supports = businessSupportMapper.listBusinessSupportByEmpId(userInfo.getId());
        Set<Integer> deptResultSet = new LinkedHashSet<>();
        //当未设置专管列表时，默认只查询出当前user所属网点及下级
        if (CollectionUtils.isEmpty(supports)) {
            Set<Integer> rd = departmentRecursion(deptResultSet, userInfo.getDepartmentId());
            deptResultSet.addAll(rd);
        } else {
            for (BusinessSupport bs : supports) {
                Set<Integer> set = departmentRecursion(deptResultSet, bs.getDepartmentId());
                deptResultSet.addAll(set);
            }
        }
        if (CollectionUtils.isEmpty(deptResultSet)) {
            return null;
        } else {
            return new ArrayList<>(deptResultSet);
        }
    }

    /**
     * 获取指定部门id及下属部门id数组
     *
     * @return
     */
    @Override
    public List<Integer> getDownDepartmentByDeptId(Integer deptId) {
        Set<Integer> deptIdSet = new LinkedHashSet<>();
        Set<Integer> set = departmentRecursion(deptIdSet, deptId);
        if (CollectionUtils.isEmpty(set)) {
            return null;
        } else {
            return new ArrayList<>(set);
        }
    }

    /**
     * 查询当前用户的本部门及本部门以下的部门
     *
     * @return
     */
    @Override
    public Map<String, Object> getDownDepartmentByCurrentUser() {
        String token = request.getHeader("token");
        UserInfo userInfo = authClientService.getUserByToken(token);
        if (userInfo == null) {
            return ServiceResult.toResult(null);
        }
        List<ListDepartmentDto> departmentDtoList = departmentMapper.listDepartmentByIds(userInfo.getDepartmentId());
        if (CollectionUtils.isEmpty(departmentDtoList)) {
            return ServiceResult.toResult(null);
        }
        return ServiceResult.toResult(departmentDtoList);
    }

    private Set<Integer> departmentRecursion(Set<Integer> deptResultSet, Integer did) {
        if (null != did) {
            deptResultSet.add(did);
        }
        //找到所有第一层子部门列表
        List<Integer> deptIds = departmentMapper.getDownDepartmentId(did);
        if (deptIds.size() != 0) {
            for (Integer deptId : deptIds) {
                departmentRecursion(deptResultSet, deptId);
            }
        }
        log.info("当前用户可查询的部门id： " + deptResultSet);
        return deptResultSet;
    }

    private Set<Integer> departmentRecursion2(Set<Integer> deptResultSet, Integer did) {
        //找到所有第一层子部门列表
        List<Integer> deptIds = departmentMapper.getDownDepartmentId(did);
        if (deptIds.size() != 0) {
            for (Integer deptId : deptIds) {
                departmentRecursion(deptResultSet, deptId);
            }
        }
        log.info("指定部门的下属id： " + deptResultSet);
        return deptResultSet;
    }

    /**
     * 获取所有部门 供其它系统使用
     *
     * @return
     * @Author gavin 20181203
     */
    @Override
    public List<DepartmentReturnDto> getAllDepartments() {
        return departmentMapper.getAllDepartments();
    }
}
