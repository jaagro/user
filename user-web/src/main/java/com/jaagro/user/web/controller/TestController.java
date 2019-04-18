package com.jaagro.user.web.controller;

import com.jaagro.constant.UserInfo;
import com.jaagro.user.api.dto.response.DepartmentReturnDto;
import com.jaagro.user.api.service.DepartmentService;
import com.jaagro.user.api.service.UserService;
import com.jaagro.user.biz.config.UserIdGeneratorFactory;
import com.jaagro.user.biz.mapper.DepartmentMapperExt;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.MD5Utils;
import com.jaagro.utils.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
@RestController
public class TestController {

    @Autowired
    UserService userService;
    @Autowired
    DepartmentMapperExt departmentMapper;
    @Autowired
    DepartmentService departmentService;

    @GetMapping("/test")
    public UserInfo test(){
        return userService.getCurrentUser();
    }

    @GetMapping("/test2")
    public DepartmentReturnDto test2(){
        return departmentMapper.getById(1);
    }

    @Autowired
    UserIdGeneratorFactory userIdGeneratorFactory;

    @GetMapping("/test3")
    public int test3(){
        return userIdGeneratorFactory.getNextId();
    }

    @GetMapping("/test4")
    public List<Integer> test4(){
        return departmentService.getDownDepartment();
    }

    @GetMapping("/testGetRegionByNetworkId/{networkId}")
    public BaseResponse<DepartmentReturnDto> getRegionByNetworkId(@PathVariable("networkId") Integer networkId){
        return BaseResponse.successInstance(departmentService.getRegionByNetworkId(networkId));
    }

    public static void main(String args[]){
//        Map<String, String> stringMap = PasswordEncoder.encodePassword("123");
//        System.err.println(PasswordEncoder.encodePassword("123").get("salt"));
//        System.err.println(PasswordEncoder.encodePassword("123").get("password"));
//        System.err.println(MD5Utils.encode("123", "24558"));
        String encodePassword = MD5Utils.encode("456", "11675");
        System.err.println(encodePassword);
    }
}
