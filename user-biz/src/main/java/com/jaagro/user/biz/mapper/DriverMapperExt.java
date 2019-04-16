package com.jaagro.user.biz.mapper;

import com.jaagro.constant.UserInfo;
import com.jaagro.user.api.dto.request.CriteriaDriverDto;
import com.jaagro.user.api.dto.response.DriverReturnDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tony
 */
public interface DriverMapperExt extends DriverMapper {

    /**
     * 通过登录名获取司机
     *
     * @param loginName
     * @return
     */
    UserInfo getByLoginName(String loginName);

    /**
     * 通过手机号获取司机
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
     * 通过车辆id获取司机list
     *
     * @param truckId
     * @return
     */
    List<DriverReturnDto> listDriverByTruckId(@Param("truckId") Integer truckId, @Param("tenantId") Integer tenantId);

    /**
     * 删除司机【逻辑】
     *
     * @param id
     * @param status
     * @return
     */
    Integer deleteDriverLogic(@Param("status") Integer status, @Param("id") Integer id);

    /**
     * 删除车队下所有司机【逻辑】
     *
     * @param status
     * @param truckId
     * @return
     */
    Integer deleteDriverByTruckId(@Param("status") Integer status, @Param("teamId") Integer truckId);

    /**
     * 通过id获取司机
     *
     * @param id
     * @return
     */
    DriverReturnDto getDriverById(Integer id);

    /**
     * app登陆时更新司机的注册id gavin
     *
     * @param phoneNumber
     * @return
     */
    Integer updateDriverRegIdByPhoneNumber(@Param("phoneNumber") String phoneNumber, @Param("registrationId") String registrationId);

    /**
     * id列表获取用户信息列表
     *
     * @param userIdList
     * @return
     */
    List<UserInfo> listUserInfo(@Param("userIdList") List<Integer> userIdList);

    /**
     * 查询近一个月过期证件
     *
     * @param expiryDateType
     * @return
     */
    List<DriverReturnDto> listCertificateOverdueNotice(@Param("expiryDateType") Integer expiryDateType, @Param("tenantId") Integer tenantId);

    /**
     * 批量查询司机信息 不区分状态
     * @author yj
     * @param driverIdList
     * @return
     */
    List<DriverReturnDto> listDriverByIds(@Param("driverIdList") List<Integer> driverIdList);

    /**
     * 根据手机号查询
     * @author yj
     * @param phoneNumber
     * @return
     */
    DriverReturnDto selectByPhoneNumber(CriteriaDriverDto driverDto);
}