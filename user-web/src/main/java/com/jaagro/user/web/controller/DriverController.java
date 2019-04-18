package com.jaagro.user.web.controller;

import com.jaagro.user.api.dto.request.CreateDriverDto;
import com.jaagro.user.api.dto.request.UpdateDriverDto;
import com.jaagro.user.api.dto.response.DriverReturnDto;
import com.jaagro.user.api.service.DriverService;
import com.jaagro.utils.BaseResponse;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author tony
 */
@RestController
public class DriverController {

    @Autowired
    private DriverService driverService;

    /**
     * 新增司机
     *
     * @param driver
     * @return
     */
    @PostMapping("/driver")
    @ApiOperation("新增司机")
    public BaseResponse createDriver(@RequestBody CreateDriverDto driver) {
        return BaseResponse.service(driverService.createDriver(driver));
    }

    /**
     * 查询该车辆名下的司机
     *
     * @param truckId
     * @return
     */
    @ApiOperation("查询该车辆名下的司机")
    @GetMapping("/listDriverByTruckId/{truckId}")
    public List<DriverReturnDto> listByTruckId(@PathVariable("truckId") Integer truckId) {
        return driverService.listByTruckId(truckId);
    }

    /**
     * 查询单个司机
     *
     * @param id
     * @return
     */
    @ApiOperation("查询单个司机")
    @GetMapping("/driver/{id}")
    public BaseResponse getDriverById(@PathVariable("id") Integer id) {
        return BaseResponse.service(driverService.getById(id));
    }

    /**
     * 查询单个司机
     *
     * @param id
     * @return
     */
    @Ignore
    @GetMapping("/driverFeign/{id}")
    public DriverReturnDto getDriverReturnObject(@PathVariable("id") Integer id) {
        return driverService.getDriverReturnObject(id);
    }

    /**
     * 查询单个司机
     *
     * @param id
     * @return
     */
    @Ignore
    @GetMapping("/getDriverByIdFeign/{id}")
    public DriverReturnDto getDriverByIdFeign(@PathVariable("id") Integer id) {
        return driverService.getDriverById(id);
    }

    /**
     * 新增司机并返回id
     *
     * @param driver
     * @return
     */
    @Ignore
    @PostMapping("/driverFeign")
    public Integer createDriverReturnId(@RequestBody CreateDriverDto driver) {
        return driverService.createDriverReturnId(driver);
    }

    /**
     * 修改司机
     *
     * @param driver
     * @return
     */
    @Ignore
    @PostMapping("/updateDriverFeign")
    public BaseResponse updateDriverFeign(@RequestBody UpdateDriverDto driver) {
        return BaseResponse.service(driverService.updateDriver(driver));
    }

    /**
     * 删除司机[逻辑删除]
     *
     * @param id
     * @return
     */
    @ApiOperation("删除司机[逻辑删除]")
    @PostMapping("/driver/{id}")
    public BaseResponse deleteDriver(@PathVariable("id") Integer id) {
        return BaseResponse.service(driverService.deleteDriver(id));
    }

    /**
     * 删除该车辆下的司机[逻辑删除]
     *
     * @param truckId
     * @return
     */
    @ApiOperation("删除该车辆下的司机[逻辑删除]")
    @DeleteMapping("/driverByTruck/{truckId}")
    public BaseResponse deleteDriverByTruckId(@PathVariable("truckId") Integer truckId) {
        return BaseResponse.service(driverService.deleteDriverByTruckId(truckId));
    }

    @Ignore
    @PostMapping("/updateDriverStatusFeign/{driverId}")
    public BaseResponse updateDriverStatusFeign(@PathVariable("driverId") Integer driverId) {
        return BaseResponse.service(driverService.updateDriverStatus(driverId));
    }

    @Ignore
    @PostMapping("/updateDriverByPhoneNumber")
    public BaseResponse updateDriverByPhoneNumber(@RequestBody UpdateDriverDto driver) {
        return BaseResponse.service(driverService.updateDriverRegIdByPhoneNumber(driver));
    }

    /**
     * 查询司机近一个月过期证件
     *
     * @param expiryDateType
     * @return
     */
    @ApiOperation("查询司机近一个月过期证件")
    @GetMapping("/listDriverCertificateOverdueNotice/{expiryDateType}")
    public BaseResponse listDriverCertificateOverdueNotice(@PathVariable("expiryDateType") Integer expiryDateType) {
        return BaseResponse.successInstance(driverService.listCertificateOverdueNotice(expiryDateType));
    }

    /**
     * 批量查询司机信息 不区分状态
     *
     * @param driverIdList
     * @return
     */
    @ApiOperation("批量查询司机信息 不区分状态")
    @GetMapping("/listDriverByIds")
    public BaseResponse<List<DriverReturnDto>> listDriverByIds(@RequestParam("driverIdList") List<Integer> driverIdList) {
        return BaseResponse.successInstance(driverService.listDriverByIds(driverIdList));
    }

    @ApiOperation("通过手机号查询司机")
    @GetMapping("/getByPhoneNumber")
    public BaseResponse<DriverReturnDto> getByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        return BaseResponse.successInstance(driverService.getByPhoneNumber(phoneNumber));
    }
}
