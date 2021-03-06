package com.jaagro.user.web.controller;

import com.jaagro.user.api.dto.request.CreateCustomerUserDto;
import com.jaagro.user.api.dto.response.GetCustomerUserDto;
import com.jaagro.user.api.service.CustomerUserService;
import com.jaagro.utils.BaseResponse;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
     * @param userDto
     * @return
     */
    @Ignore
    @PostMapping("/customerUser")
    public BaseResponse createCustomerUser(@RequestBody List<CreateCustomerUserDto> userDto) {
        Map<String, Object> resultMap;
        try {
            resultMap = customerUserService.createCustomerUser(userDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            return BaseResponse.errorInstance(ex.getMessage());
        }
        return BaseResponse.service(resultMap);
    }

}
