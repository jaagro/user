package com.jaagro.user.biz.service.impl;

import com.jaagro.constant.UserInfo;
import com.jaagro.user.api.dto.request.CreateCustomerUserDto;
import com.jaagro.user.api.dto.request.UpdateCustomerUserDto;
import com.jaagro.user.api.dto.response.GetCustomerUserDto;
import com.jaagro.user.api.service.CustomerUserService;
import com.jaagro.user.biz.entity.CustomerUser;
import com.jaagro.user.biz.mapper.CustomerUserMapperExt;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
@Service
public class CustomerUserServiceImpl implements CustomerUserService {

    @Autowired
    private CustomerUserMapperExt customerUserMapperExt;

    /**
     * 根据id获取customerUser
     *
     * @param id
     * @return
     */
    @Override
    public GetCustomerUserDto getCustomerUserById(Integer id) {
        CustomerUser customerUser = customerUserMapperExt.selectByPrimaryKey(id);
        if (customerUser == null) {
            throw new NullPointerException("当前用户id: " + id + "不存在");
        }
        GetCustomerUserDto getCustomerUserDto = new GetCustomerUserDto();
        BeanUtils.copyProperties(customerUser, getCustomerUserDto);
        return getCustomerUserDto;
    }

    /**
     * 根据手机号查询
     *
     * @param phoneNumber
     * @return
     */
    @Override
    public GetCustomerUserDto getByPhoneNumber(String phoneNumber) {
        CustomerUser customerUser = customerUserMapperExt.selectByPhoneNumber(phoneNumber);
        return convertToDto(customerUser);
    }

    /**
     * 根据关联客户id查询
     *
     * @param relevanceId
     * @return
     */
    @Override
    public GetCustomerUserDto getCustomerUserByRelevanceId(Integer relevanceId) {
        CustomerUser customerUser = customerUserMapperExt.selectByRelevanceId(relevanceId);
        return convertToDto(customerUser);
    }

    /**
     * 创建customerUser
     *
     * @param userDtoList
     * @return
     */
    @Override
    public Map<String, Object> createCustomerUser(CreateCustomerUserDto customerUserDto) {
        if (StringUtils.isEmpty(customerUserDto.getPhoneNumber())) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "联系电话不能为空");
        }
        if (StringUtils.isEmpty(customerUserDto.getCustomerType())) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "养殖户类型不能为空");
        }
        if (StringUtils.isEmpty(customerUserDto.getRelevanceId())) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "关联id不能为空");
        }
        if (StringUtils.isEmpty(customerUserDto.getTenantId())) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "tenantId不能为空");
        }
        CustomerUser customerUser = new CustomerUser();
        BeanUtils.copyProperties(customerUserDto, customerUser);
        //判断手机号是否已存在
        UserInfo userInfo = customerUserMapperExt.getByPhoneNumber(customerUser.getPhoneNumber());
        if (userInfo != null) {
            return ServiceResult.error("手机号重复");
        }
        customerUser
                .setSalt("42850")
                .setPassword("da64f37c606c762a7e7d05d8a8a4e2dc");
        customerUserMapperExt.insertSelective(customerUser);
        return ServiceResult.toResult("操作成功");
    }

    /**
     * 修改
     *
     * @param customerUserDto
     * @return
     */
    @Override
    public Map<String, Object> updateCustomerUser(UpdateCustomerUserDto customerUserDto) {
        //判断手机号是否已存在
        UserInfo userInfo = customerUserMapperExt.getByPhoneNumber(customerUserDto.getPhoneNumber());
        if (userInfo != null) {
            return ServiceResult.error("手机号重复");
        }
        CustomerUser customerUser = customerUserMapperExt.selectByStandbyId(customerUserDto.getStandbyId());
        if (customerUser == null) {
            return ServiceResult.error(customerUserDto.getStandbyId()+"账号不存在");
        }
        CustomerUser user = new CustomerUser();
        BeanUtils.copyProperties(customerUserDto, user);
        user.setId(customerUser.getId());
        customerUserMapperExt.updateByPrimaryKeySelective(user);
        return ServiceResult.toResult("修改成功");
    }

    /**
     * 根据联系人id删除登录账号
     *
     * @param standbyId
     * @return
     */
    @Override
    public Map<String, Object> deleteByStandbyId(Integer standbyId) {
        customerUserMapperExt.deleteByStandbyId(standbyId);
        return ServiceResult.toResult("删除成功");
    }

    private GetCustomerUserDto convertToDto(CustomerUser customerUser) {
        if (customerUser != null) {
            GetCustomerUserDto customerUserReturnDto = new GetCustomerUserDto();
            BeanUtils.copyProperties(customerUser, customerUserReturnDto);
            return customerUserReturnDto;
        }
        return null;
    }

}
