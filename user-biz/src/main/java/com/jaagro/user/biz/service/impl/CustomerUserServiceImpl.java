package com.jaagro.user.biz.service.impl;

import com.jaagro.constant.UserInfo;
import com.jaagro.user.api.dto.request.CreateCustomerUserDto;
import com.jaagro.user.api.dto.response.GetCustomerUserDto;
import com.jaagro.user.api.service.CustomerUserService;
import com.jaagro.user.biz.entity.CustomerUser;
import com.jaagro.user.biz.mapper.CustomerUserMapperExt;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ServiceResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * @param userDto
     * @return
     */
    @Override
    public Map<String, Object> createCustomerUser(CreateCustomerUserDto userDto) {
        CustomerUser customerUser = new CustomerUser();
        BeanUtils.copyProperties(userDto, customerUser);
        //判断手机号是否已存在
        UserInfo userInfo = customerUserMapperExt.getByPhoneNumber(customerUser.getPhoneNumber());
        if (userInfo != null) {
            return ServiceResult.error("手机号重复");
        }
        customerUser
                .setSalt("42850")
                .setPassword("da64f37c606c762a7e7d05d8a8a4e2dc");
        int result = customerUserMapperExt.insertSelective(customerUser);
        if (result > 0) {
            return ServiceResult.toResult("操作成功");
        } else {
            return ServiceResult.error("操作失败");
        }
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
