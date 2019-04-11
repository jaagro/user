package com.jaagro.user.web.controller;

import com.jaagro.user.api.dto.request.CreateCustomerUserDto;
import com.jaagro.user.api.dto.request.UpdateCustomerUserDto;
import com.jaagro.user.api.dto.response.GetCustomerUserDto;
import com.jaagro.user.api.service.CustomerUserService;
import com.jaagro.utils.BaseResponse;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author tony
 */
@RestController
public class CustomerUserController {

    @Autowired
    private CustomerUserService customerUserService;

    @GetMapping("/customerUser/{id}")
    public GetCustomerUserDto getCustomerUserById(@PathVariable("id") Integer id) {
        return customerUserService.getCustomerUserById(id);
    }

    @GetMapping("/getCustomerUserByPhoneNumber")
    public BaseResponse<GetCustomerUserDto> getCustomerUserByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        GetCustomerUserDto getCustomerUserDto = customerUserService.getByPhoneNumber(phoneNumber);
        if (getCustomerUserDto != null) {
            return BaseResponse.successInstance(getCustomerUserDto);
        }
        return BaseResponse.queryDataEmpty();
    }

    @GetMapping("/getCustomerUserByRelevanceId/{relevanceId}")
    public BaseResponse<GetCustomerUserDto> getCustomerUserByRelevanceId(@PathVariable("relevanceId") Integer relevanceId) {
        GetCustomerUserDto getCustomerUserDto = customerUserService.getCustomerUserByRelevanceId(relevanceId);
        if (getCustomerUserDto != null) {
            return BaseResponse.successInstance(getCustomerUserDto);
        }
        return BaseResponse.idError();
    }

    /**
     * 新增养殖户app登录用户
     *
     * @param customerUserDto
     * @return
     */
    @Ignore
    @PostMapping("/customerUser")
    public BaseResponse createCustomerUser(@RequestBody CreateCustomerUserDto customerUserDto) {
        Map<String, Object> resultMap;
        try {
            resultMap = customerUserService.createCustomerUser(customerUserDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            return BaseResponse.errorInstance(customerUserDto.getPhoneNumber() + "手机号重复");
        }
        return BaseResponse.service(resultMap);
    }

    /**
     * 修改养殖户app登录用户
     *
     * @param customerUserDto
     * @return
     */
    @Ignore
    @PostMapping("/updateCustomerUser")
    public BaseResponse updateCustomerUser(@RequestBody UpdateCustomerUserDto customerUserDto) {
        if (StringUtils.isEmpty(customerUserDto.getStandbyId())) {
            return BaseResponse.errorInstance("联系人id不能为空");
        }
        Map<String, Object> resultMap;
        try {
            resultMap = customerUserService.updateCustomerUser(customerUserDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            return BaseResponse.errorInstance(customerUserDto.getPhoneNumber() + "手机号重复");
        }
        return BaseResponse.service(resultMap);
    }

    /**
     * 根据联系人id删除登录账号
     *
     * @param standbyId
     * @return
     */
    @Ignore
    @GetMapping("/deleteByStandbyId/{standbyId}")
    public BaseResponse deleteByStandbyId(@PathVariable("standbyId") Integer standbyId) {
        return BaseResponse.service(customerUserService.deleteByStandbyId(standbyId));
    }

}
