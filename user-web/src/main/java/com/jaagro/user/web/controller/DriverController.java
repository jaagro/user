package com.jaagro.user.web.controller;

import com.jaagro.user.api.dto.request.CreateDriverDto;
import com.jaagro.user.api.dto.response.DriverReturnDto;
import com.jaagro.user.api.service.DriverService;
import com.jaagro.utils.BaseResponse;
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

    @PostMapping("/driver")
    public BaseResponse createDriver(@RequestBody CreateDriverDto driver){

        BaseResponse result;
        try {
            result = BaseResponse.service(driverService.createDriver(driver));
        }catch (Exception e) {
            throw e;
        }
        return result;
    }

    @GetMapping("/listDriverByTruckId/{truckId}")
    public List<DriverReturnDto> listByTruckId(@PathVariable("truckId") Integer truckId){
        return driverService.listByTruckId(truckId);
    }

    @Ignore
    @PostMapping("/driverFeign")
    public Integer createDriverToFeign(@RequestBody CreateDriverDto driver){
        Integer result;
        try {
            result = driverService.createDriverToFeign(driver);
        }catch (Exception e) {
            throw e;
        }
        return result;
    }
}
