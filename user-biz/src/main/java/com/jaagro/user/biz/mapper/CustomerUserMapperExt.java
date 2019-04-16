package com.jaagro.user.biz.mapper;

import com.jaagro.constant.UserInfo;
import com.jaagro.user.api.dto.request.CriteriaDto;
import com.jaagro.user.api.dto.response.GetCustomerUserDto;
import com.jaagro.user.biz.entity.CustomerUser;
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
     *
     * @param userIdList
     * @return
     */
    List<UserInfo> listUserInfo(@Param("userIdList") List<Integer> userIdList);

    /**
     * 获取单条有效客户user
     *
     * @param id
     * @return
     */
    GetCustomerUserDto getCustomerUserById(int id);

    /**
     * 根据手机号查询
     *
     * @param criteriaDto
     * @return
     */
    CustomerUser selectByPhoneNumber(CriteriaDto criteriaDto);

    /**
     * 根据关联客户id查询
     *
     * @param relevanceId
     * @return
     */
    CustomerUser selectByRelevanceId(@Param("relevanceId") Integer relevanceId, @Param("tenantId") Integer tenantId);

    /**
     * 根据关联id删除
     *
     * @param relevanceId
     * @return
     */
    int deleteByCustomerId(@Param("relevanceId") Integer relevanceId);

    /**
     * 根据standbyId查询
     *
     * @param standbyId
     * @return
     */
    CustomerUser selectByStandbyId(@Param("standbyId") Integer standbyId);

    /**
     * 根据联系人id删除登录账号
     *
     * @param standbyId
     * @return
     */
    int deleteByStandbyId(@Param("standbyId") Integer standbyId);

    /**
     * 根据手机号查询
     * 如果是新增 查询不包括当前id的
     *
     * @param customerUser
     * @return
     */
    CustomerUser selectByPhoneNotStandbyId(CustomerUser customerUser);
}