package com.jaagro.user.api.service;

import com.jaagro.user.api.dto.request.CreateCustomerUserDto;
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
     * @param userDto
     * @return
     */
    Map<String, Object> createCustomerUser(CreateCustomerUserDto userDto);

    /**
     * 修改 养殖账号电话
     *
     * @param createCustomerUserDtoList
     * @return
     */
    Map<String, Object> updateCustomerUser(List<CreateCustomerUserDto> createCustomerUserDtoList);
}
