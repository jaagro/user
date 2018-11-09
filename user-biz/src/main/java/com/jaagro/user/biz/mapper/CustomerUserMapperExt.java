package com.jaagro.user.biz.mapper;

import com.jaagro.constant.UserInfo;
import com.jaagro.user.api.dto.response.GetCustomerUserDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tony
 */
public interface CustomerUserMapperExt extends CustomerUserMapper {
    /**
     * 通过登录名获取userInfo
     *
     * @param loginName
     * @return
     */
    UserInfo getByLoginName(String loginName);

    /**
     * 通过手机号码获取userInfo
     *
     * @param phoneNumber
     * @return
     */
    UserInfo getByPhoneNumber(String phoneNumber);

    /**
     * id获取userInfo
     *
     * @param id
     * @return
     */
    UserInfo getUserInfoById(Integer id);

    /**
     * id列表获取userInfo列表
     * @param userIdList
     * @return
     */
    List<UserInfo> listUserInfo(@Param("userIdList") List<Integer> userIdList);
}