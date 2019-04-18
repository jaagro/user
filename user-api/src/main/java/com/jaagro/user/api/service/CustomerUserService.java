package com.jaagro.user.api.service;

import com.jaagro.user.api.dto.request.CreateCustomerUserDto;
import com.jaagro.user.api.dto.request.UpdateCustomerUserDto;
import com.jaagro.user.api.dto.response.GetCustomerUserDto;

import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
public interface CustomerUserService {

    /**
     * 根据id获取customerUser
     *
     * @param id
     * @return
     */
    GetCustomerUserDto getCustomerUserById(Integer id);

    /**
     * 根据手机号查询
     *
     * @param phoneNumber
     * @return
     */
    GetCustomerUserDto getByPhoneNumber(String phoneNumber);

    /**
     * 根据关联客户id查询
     *
     * @param relevanceId
     * @return
     */
    GetCustomerUserDto getCustomerUserByRelevanceId(Integer relevanceId);

    /**
     * 创建customerUser
     *
     * @param customerUserDto
     * @return
     */
    Map<String, Object> createCustomerUser(CreateCustomerUserDto customerUserDto);

    /**
     * 修改
     *
     * @param customerUserDto
     * @return
     */
    Map<String, Object> updateCustomerUser(UpdateCustomerUserDto customerUserDto);

    /**
     * 根据联系人id删除登录账号
     *
     * @param standbyId
     * @return
     */
    Map<String, Object> deleteByStandbyId(Integer standbyId);
}
