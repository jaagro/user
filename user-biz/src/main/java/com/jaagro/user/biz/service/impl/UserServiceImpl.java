package com.jaagro.user.biz.service.impl;

import com.jaagro.constant.UserInfo;
import com.jaagro.user.api.constant.CustomerType;
import com.jaagro.user.api.constant.UserType;
import com.jaagro.user.api.dto.GetTenantDto;
import com.jaagro.user.api.dto.base.ShowSiteDto;
import com.jaagro.user.api.dto.response.CustomerRegisterPurposeDto;
import com.jaagro.user.api.dto.response.DriverReturnDto;
import com.jaagro.user.api.dto.response.GetCustomerUserDto;
import com.jaagro.user.api.dto.response.SocialDriverRegisterPurposeDto;
import com.jaagro.user.api.dto.response.employee.GetEmployeeDto;
import com.jaagro.user.api.service.CrmClientService;
import com.jaagro.user.api.service.CustomerUserService;
import com.jaagro.user.api.service.UserClientService;
import com.jaagro.user.api.service.UserService;
import com.jaagro.user.biz.mapper.CustomerUserMapperExt;
import com.jaagro.user.biz.mapper.DriverMapperExt;
import com.jaagro.user.biz.mapper.EmployeeMapperExt;
import com.jaagro.utils.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String KEY = "key";
    private static final String LOGIN_TYPE = "loginType";

    private static final String LOGIN_NAME = "loginName";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String ID = "id";

    private static final String USER_TYPE = "userType";

    @Autowired
    private CustomerUserMapperExt customerUserMapper;
    @Autowired
    private EmployeeMapperExt employeeMapper;
    @Autowired
    private DriverMapperExt driverMapper;
    @Autowired
    private UserClientService userClientService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CrmClientService crmClientService;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserInfo getUserInfo(Map<String, Object> map) {
        String userTypeTrim = map.get(USER_TYPE).toString().replaceAll(" ", "");
        String loginType = (String) map.get(LOGIN_TYPE);
        UserInfo userInfo = null;
        boolean isCustomer = UserType.CUSTOMER.equals(userTypeTrim) ||
                UserType.VISITOR_CUSTOMER_P.equals(userTypeTrim) ||
                UserType.VISITOR_CUSTOMER_U.equals(userTypeTrim) ||
                UserType.LOAD_SITE.equals(userTypeTrim) ||
                UserType.UNLOAD_SITE.equals(userTypeTrim) ||
                UserType.FARMERS.equals(userTypeTrim);
        if (isCustomer) {
            if (LOGIN_NAME.equals(loginType)) {
                userInfo = customerUserMapper.getByLoginName(parseKey(map));
                if (userInfo != null) {
                    userInfo = this.updateUserInfoBusinessType(this.updateUserInfoUserType(userInfo));
                }
            }
            if (PHONE_NUMBER.equals(loginType)) {
                userInfo = customerUserMapper.getByPhoneNumber(parseKey(map));
                if (userInfo != null) {
                    userInfo = this.updateUserInfoBusinessType(this.updateUserInfoUserType(userInfo));
                } else {
                    userInfo = this.getCustomerRegisterPurpose(map);
                }
            }
            if (ID.equals(loginType)) {
                userInfo = customerUserMapper.getUserInfoById(parseKey(map));
                if (userInfo != null) {
                    userInfo = this.updateUserInfoBusinessType(this.updateUserInfoUserType(userInfo));
                } else {
                    //游客暂不支持tenant，但是稍后需要调整游客表并加入tenantId
                    userInfo = this.getCustomerRegisterPurpose(map);
                }
            }
        }
        if (UserType.EMPLOYEE.equals(userTypeTrim)) {
            if (LOGIN_NAME.equals(loginType)) {
                userInfo = this.updateUserInfoBusinessType(employeeMapper.getByLoginName(parseKey(map)));
            }
            if (PHONE_NUMBER.equals(loginType)) {
                userInfo = this.updateUserInfoBusinessType(employeeMapper.getByPhone(parseKey(map)));
            }
            if (ID.equals(loginType)) {
                userInfo = this.updateUserInfoBusinessType(employeeMapper.getUserInfoById(parseKey(map)));
            }
        }
        if (UserType.DRIVER.equals(userTypeTrim) ||
                UserType.VISITOR_DRIVER_P.equals(userTypeTrim) ||
                UserType.VISITOR_DRIVER_U.equals(userTypeTrim)) {
            if (LOGIN_NAME.equals(loginType)) {
                userInfo = this.updateUserInfoBusinessType(driverMapper.getByLoginName(parseKey(map)));
            }
            if (PHONE_NUMBER.equals(loginType)) {
                userInfo = this.updateUserInfoBusinessType(driverMapper.getByPhoneNumber(parseKey(map)));
                if (null == userInfo) {
                    userInfo = this.getSocialDriverRegisterPurpose(map);
                }
            }
            if (ID.equals(loginType)) {
                userInfo = this.updateUserInfoBusinessType(driverMapper.getUserInfoById(parseKey(map)));
                if (null == userInfo) {
                    userInfo = this.getSocialDriverRegisterPurpose(map);
                }
            }
        }
        if (null != userInfo && StringUtils.isEmpty(userInfo.getUserType())) {
            userInfo.setUserType(userTypeTrim);
            if (UserType.VISITOR_DRIVER_P.equals(userInfo.getUserType()) || UserType.VISITOR_DRIVER_U.equals(userInfo.getUserType())) {
                log.info("O getUserInfo: The current driver is a visitor：{}", userInfo);
            }
            if (UserType.VISITOR_CUSTOMER_P.equals(userInfo.getUserType()) || UserType.VISITOR_CUSTOMER_U.equals(userInfo.getUserType())) {
                log.info("O getUserInfo: The current customer is a visitor：{}", userInfo);
            }
            if (UserType.LOAD_SITE.equals(userInfo.getUserType()) || UserType.UNLOAD_SITE.equals(userInfo.getUserType())) {
                log.info("O getUserInfo: The current site customer is a visitor：{}", userInfo);
            }
        }
        return userInfo;
    }

    /**
     * 全局获取user
     *
     * @param userId
     * @return
     * @author tony
     */
    @Override
    public UserInfo getGlobalUser(int userId) {
        UserInfo userInfo = new UserInfo();
        GetEmployeeDto employee = employeeMapper.getById(userId);
        if (null == employee) {
            DriverReturnDto driver = driverMapper.getDriverById(userId);
            if (null == driver) {
                GetCustomerUserDto customerUser = customerUserMapper.getCustomerUserById(userId);
                if (null == customerUser) {
                    SocialDriverRegisterPurposeDto sdr = crmClientService.getSocialDriverRegisterPurposeDtoById(userId).getData();
                    if (null == sdr) {
                        throw new NullPointerException("not record");
                    } else {
                        userInfo.setId(sdr.getId());
                        userInfo.setName(sdr.getName());
                        userInfo.setPhoneNumber(sdr.getPhoneNumber());
                        if (sdr.getUploadFlag() != null && sdr.getUploadFlag().equals(Boolean.TRUE)) {
                            userInfo.setUserType(UserType.VISITOR_DRIVER_U);
                        } else {
                            userInfo.setUserType(UserType.VISITOR_DRIVER_P);
                        }
                    }
                } else {
                    CustomerRegisterPurposeDto sdr = crmClientService.getCustomerRegisterPurposeDtoById(userId).getData();
                    if (sdr != null) {
                        userInfo.setName(sdr.getName());
                        userInfo.setId(sdr.getId());
                        userInfo.setPhoneNumber(sdr.getPhoneNumber());
                        if (sdr.getUploadFlag() != null && sdr.getUploadFlag().equals(Boolean.TRUE)) {
                            userInfo.setUserType(UserType.VISITOR_CUSTOMER_U);
                        } else {
                            userInfo.setUserType(UserType.VISITOR_CUSTOMER_P);
                        }
                    } else {
                        BeanUtils.copyProperties(customerUser, userInfo);
                        userInfo.setUserType(UserType.CUSTOMER);
                    }
                }
            } else {
                BeanUtils.copyProperties(driver, userInfo);
                userInfo.setUserType(UserType.DRIVER);
            }
        } else {
            BeanUtils.copyProperties(employee, userInfo);
            userInfo.setPhoneNumber(employee.getPhone());
            userInfo.setUserType(UserType.EMPLOYEE);
        }
        return userInfo;
    }

    /**
     * 批量获取全局user
     *
     * @param userIds
     * @return
     * @author tony
     */
    @Override
    public List<UserInfo> listGlobalUser(List<Integer> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            throw new NullPointerException("userIds must not be null");
        }
        if (userIds.size() > 30) {
            throw new RuntimeException(userIds.size() + " :The maximum number of queries is 30");
        }
        List<UserInfo> userInfoList = new ArrayList<>();
        for (int id : userIds) {
            UserInfo userInfo = this.getGlobalUser(id);
            if (null != userInfo) {
                userInfoList.add(userInfo);
            }
        }
        return userInfoList;
    }

    /**
     * 获取游客身份司机
     *
     * @param map
     * @return
     */
    private UserInfo getSocialDriverRegisterPurpose(Map<String, Object> map) {
        SocialDriverRegisterPurposeDto sdr;
        if (ID.equals(map.get(LOGIN_TYPE).toString())) {
            sdr = crmClientService.getSocialDriverRegisterPurposeDtoById(parseKey(map)).getData();
        } else {
            sdr = crmClientService.getByPhoneNumber(parseKey(map)).getData();
        }
        if (null != sdr) {
            UserInfo userInfo = new UserInfo();
            userInfo.setName(sdr.getName());
            userInfo.setId(sdr.getId());
            userInfo.setPhoneNumber(sdr.getPhoneNumber());
            if (sdr.getUploadFlag() != null && sdr.getUploadFlag().equals(Boolean.TRUE)) {
                userInfo.setUserType(UserType.VISITOR_DRIVER_U);
            } else {
                userInfo.setUserType(UserType.VISITOR_DRIVER_P);
            }
            return userInfo;
        }
        return null;
    }

    /**
     * 填充UserInfo的 UserTyp 字段，适用于user 客户域
     *
     * @param userInfo
     * @return
     */
    private UserInfo updateUserInfoUserType(UserInfo userInfo) {
        if (null == userInfo) {
            return null;
        }
        if (CustomerType.CUSTOMER.toString().equals(userInfo.getUserType())) {
            userInfo.setUserType(UserType.CUSTOMER);
        } else if (CustomerType.LOAD_SITE.toString().equals(userInfo.getUserType())) {
            userInfo.setUserType(UserType.LOAD_SITE);
        } else if (CustomerType.UNLOAD_SITE.toString().equals(userInfo.getUserType())) {
            userInfo.setUserType(UserType.UNLOAD_SITE);
        } else if (CustomerType.FARMERS.toString().equals(userInfo.getUserType())) {
            userInfo.setUserType(UserType.FARMERS);
        }
        return userInfo;
    }

    /**
     * 填充userInfo的 BusinessType 字段， 适用于user 全局
     *
     * @param userInfo
     * @return
     */
    private UserInfo updateUserInfoBusinessType(UserInfo userInfo) {
        if (null == userInfo) {
            return null;
        }
        BaseResponse<GetTenantDto> baseResponse = crmClientService.getTenantById(userInfo.getTenantId());
        GetTenantDto tenantDto = baseResponse.getData();
        if (null == tenantDto) {
            return userInfo;
        }
        userInfo.setBusinessType(tenantDto.getTenantType());
        return userInfo;
    }

    /**
     * 获取游客身份司机
     *
     * @param map
     * @return
     */
    private UserInfo getCustomerRegisterPurpose(Map<String, Object> map) {
        CustomerRegisterPurposeDto sdr;
        if (ID.equals(map.get(LOGIN_TYPE).toString())) {
            sdr = crmClientService.getCustomerRegisterPurposeDtoById(parseKey(map)).getData();
        } else {
            sdr = crmClientService.getCustomerRegisterPurposeByPhoneNumber(parseKey(map)).getData();
        }
        if (null != sdr) {
            UserInfo userInfo = new UserInfo();
            userInfo.setName(sdr.getName());
            userInfo.setId(sdr.getId());
            userInfo.setPhoneNumber(sdr.getPhoneNumber());
            if (sdr.getUploadFlag() != null && sdr.getUploadFlag().equals(Boolean.TRUE)) {
                userInfo.setUserType(UserType.VISITOR_CUSTOMER_U);
            } else {
                userInfo.setUserType(UserType.VISITOR_CUSTOMER_P);
            }
            return userInfo;
        }
        return null;
    }

    private <T> T parseKey(Map<String, Object> map) {
        if (ID.equals(map.get(LOGIN_TYPE).toString())) {
            return (T) new Integer(map.get(KEY).toString());
        } else {
            return (T) map.get(KEY).toString();
        }
    }

    @Override
    public UserInfo getCurrentUser() {
        String token = request.getHeader("token");
        return userClientService.getUserByToken(token);
    }

    /**
     * @param userIdList
     * @param userType
     * @return
     * @author yj
     * 查询用户信息列表
     */
    @Override
    public List<UserInfo> listUserInfo(List<Integer> userIdList, String userType) {
        switch (userType) {
            case UserType.CUSTOMER:
                return customerUserMapper.listUserInfo(userIdList);
            case UserType.DRIVER:
                return driverMapper.listUserInfo(userIdList);
            case UserType.EMPLOYEE:
                return employeeMapper.listUserInfo(userIdList);
            default:
                return null;
        }
    }
}

