package com.jaagro.user.biz.service.impl;

import com.jaagro.constant.UserInfo;
import com.jaagro.user.api.constant.AccountType;
import com.jaagro.user.api.constant.AccountUserType;
import com.jaagro.user.api.constant.AuditStatus;
import com.jaagro.user.api.dto.request.CreateDriverDto;
import com.jaagro.user.api.dto.request.ListDriverCriteriaDto;
import com.jaagro.user.api.dto.request.UpdateDriverDto;
import com.jaagro.user.api.dto.response.DriverReturnDto;
import com.jaagro.user.api.service.*;
import com.jaagro.user.biz.config.UserIdGeneratorFactory;
import com.jaagro.user.biz.entity.Driver;
import com.jaagro.user.biz.mapper.DriverMapperExt;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
@Service
//@CacheConfig(keyGenerator = "wiselyKeyGenerator", cacheNames = "driver")
public class DriverServiceImpl implements DriverService {

    private static final Logger log = LoggerFactory.getLogger(DriverServiceImpl.class);

    @Autowired
    private DriverMapperExt driverMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private TruckClientService truckClientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserIdGeneratorFactory userIdGeneratorFactory;
    @Autowired
    private WaybillClientService waybillClientService;

    /**
     * 新增司机
     *
     * @param driver
     * @return
     */
//    @CacheEvict(cacheNames = "driver", allEntries = true)
    @Override
    public Map<String, Object> createDriver(CreateDriverDto driver) {
        return ServiceResult.toResult(createDriverReturnId(driver));
    }

    /**
     * 提供给feign
     *
     * @param driver
     * @return
     */
//    @CacheEvict(cacheNames = "driver", allEntries = true)
    @Override
    public Integer createDriverReturnId(CreateDriverDto driver) {
        if (driverMapper.getByPhoneNumber(driver.getPhoneNumber()) != null) {
            throw new RuntimeException(driver.getPhoneNumber() + ": 当前手机号已被注册");
        }
        Driver dataDriver = new Driver();
        dataDriver.setId(userIdGeneratorFactory.getNextId());
        BeanUtils.copyProperties(driver, dataDriver);
        dataDriver.setCreateUserId(userService.getCurrentUser().getId());
        try {
            driverMapper.insertSelective(dataDriver);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("司机手机号重复" + ex.getMessage());
        }
        return dataDriver.getId();
    }

    /**
     * 修改司机
     *
     * @param driver
     * @return
     */
//    @CacheEvict(cacheNames = "driver", allEntries = true)
    @Override
    public Map<String, Object> updateDriver(UpdateDriverDto driver) {
        System.err.println("-----------司机:" + driver.toString());
        if (driver.getId() == null) {
            throw new NullPointerException("司机id不能为空");
        }
        Driver dataDriver = new Driver();
        BeanUtils.copyProperties(driver, dataDriver);
        dataDriver
                .setModifyUserId(this.userService.getCurrentUser().getId())
                .setModifyTime(new Date());
        this.driverMapper.updateByPrimaryKeySelective(dataDriver);
        return ServiceResult.toResult("修改成功");
    }

    /**
     * 修改司机手机app注册设备id
     * gavin
     *
     * @param driver
     * @return
     */
//    @CacheEvict(cacheNames = "driver", allEntries = true)
    @Override
    public Map<String, Object> updateDriverRegIdByPhoneNumber(UpdateDriverDto driver) {
        if (driver.getPhoneNumber() == null) {
            throw new NullPointerException("手机号不能为空");
        }

        if (driver.getRegistrationId() == null) {
            throw new NullPointerException("手机app注册id不能为空");
        }
        int count = this.driverMapper.updateDriverRegIdByPhoneNumber(driver.getPhoneNumber(), driver.getRegistrationId());
        if (0 == count) {
            throw new NullPointerException("更新失败，司机未注册或者需要先更新司机的手机号");
        }
        return ServiceResult.toResult("修改成功");
    }

    /**
     * 查询近一个月过期证件
     * Author: @Gao.
     *
     * @param expiryDateType
     * @return
     */
    @Override
    public List<DriverReturnDto> listCertificateOverdueNotice(Integer expiryDateType) {
        return driverMapper.listCertificateOverdueNotice(expiryDateType);
    }

    /**
     * 批量查询司机信息 不区分状态
     *
     * @param driverIdList
     * @return
     * @author yj
     */
    @Override
    public List<DriverReturnDto> listDriverByIds(List<Integer> driverIdList) {
        return driverMapper.listDriverByIds(driverIdList);
    }

    /**
     * 获取单个司机
     *
     * @param id
     * @return
     */
//    @Cacheable
    @Override
    public Map<String, Object> getById(Integer id) {
        DriverReturnDto driver = driverMapper.getDriverById(id);
        if (driver == null) {
            ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), id + " :不存在");
        }
        return ServiceResult.toResult(driver);
    }

    /**
     * 获取单个司机 返回为对象
     *
     * @param id
     * @return
     */
//    @Cacheable
    @Override
    public DriverReturnDto getDriverReturnObject(Integer id) {
        return driverMapper.getDriverById(id);
    }

    /**
     * 通过条件获取所有司机（分页）
     *
     * @param criteria
     * @return
     */
//    @Cacheable
    @Override
    public Map<String, Object> listByCriteria(ListDriverCriteriaDto criteria) {
        return null;
    }

    /**
     * 删除司机
     *
     * @param id
     * @return
     */
//    @CacheEvict(cacheNames = "driver", allEntries = true)
    @Override
    public Map<String, Object> deleteDriver(Integer id) {
        if (driverMapper.selectByPrimaryKey(id) == null) {
            throw new NullPointerException(id + " :不正确");
        }
        //当前driver有任务未完成，无法删除
        BaseResponse<Integer> result = waybillClientService.countUnFinishWaybillByDriver(id);
        if (!StringUtils.isEmpty(result)) {
            if (result.getData() > 0) {
                throw new RuntimeException("司机有未完成的任务,不得删除");
            }
        }
        driverMapper.deleteDriverLogic(AuditStatus.STOP_COOPERATION, id);
        //逻辑删除司机相关资质
        this.truckClientService.deleteTruckQualificationByDriverId(id);
        //逻辑删除账户
        accountService.deleteAccount(id, AccountUserType.DRIVER, AccountType.CASH);
        return ServiceResult.toResult(id + " :删除成功");
    }

    /**
     * 删除车队所有司机
     *
     * @param truckId
     * @return
     */
//    @CacheEvict(cacheNames = "driver", allEntries = true)
    @Override
    public Map<String, Object> deleteDriverByTruckId(Integer truckId) {
        //逻辑删除司机相关资质
        List<DriverReturnDto> driverReturnDtos = listByTruckId(truckId);
        List<Integer> userIdList = new ArrayList<Integer>();
        driverReturnDtos.forEach((driverReturnDto) -> userIdList.add(driverReturnDto.getId()));
        //后期扩展如果当前driver有任务未完成，无法删除
        driverMapper.deleteDriverByTruckId(AuditStatus.STOP_COOPERATION, truckId);
        UserInfo currentUser = userService.getCurrentUser();
        Integer currentUserId = currentUser == null ? null : currentUser.getId();
        //批量逻辑删除账户
        accountService.batchDeleteAccount(userIdList, AccountUserType.DRIVER, AccountType.CASH, currentUserId);
        if (driverReturnDtos.size() > 0) {
            for (DriverReturnDto driverReturnDto : driverReturnDtos
            ) {
                this.truckClientService.deleteTruckQualificationByDriverId(driverReturnDto.getId());
            }
        }
        return ServiceResult.toResult("车辆id为" + truckId + " :的记录删除成功");
    }

    /**
     * 通过车辆获取司机
     *
     * @param truckId
     * @return
     */
//    @Cacheable
    @Override
    public List<DriverReturnDto> listByTruckId(Integer truckId) {
        return driverMapper.listDriverByTruckId(truckId);
    }

    @Override
    public Map<String, Object> updateDriverStatus(Integer driverId) {
        Driver driver = driverMapper.selectByPrimaryKey(driverId);
        if (driver != null) {
            driver
                    .setStatus(AuditStatus.NORMAL_COOPERATION)
                    .setModifyTime(new Date())
                    .setModifyUserId(userService.getCurrentUser().getId());
            driverMapper.updateByPrimaryKeySelective(driver);
            return ServiceResult.toResult("审核司机通过成功");
        }
        return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "司机不存在");
    }

    /**
     * 根据手机号查询
     *
     * @param phoneNumber
     * @return
     */
    @Override
    public DriverReturnDto getByPhoneNumber(String phoneNumber) {
        return driverMapper.selectByPhoneNumber(phoneNumber);
    }

    /**
     * 获取司机
     *
     * @param id
     * @return
     */
    @Override
    public DriverReturnDto getDriverById(Integer id) {
        Driver driver = driverMapper.selectByPrimaryKey(id);
        if (driver != null) {
            DriverReturnDto returnDto = new DriverReturnDto();
            BeanUtils.copyProperties(driver, returnDto);
            return returnDto;
        }
        return null;
    }
}
