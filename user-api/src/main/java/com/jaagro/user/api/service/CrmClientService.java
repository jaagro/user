package com.jaagro.user.api.service;

import com.jaagro.user.api.dto.GetTenantDto;
import com.jaagro.user.api.dto.base.ShowCustomerDto;
import com.jaagro.user.api.dto.base.ShowSiteDto;
import com.jaagro.user.api.dto.response.CustomerRegisterPurposeDto;
import com.jaagro.user.api.dto.response.SocialDriverRegisterPurposeDto;
import com.jaagro.utils.BaseResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author tony
 */
@FeignClient("crm")
public interface CrmClientService {

    /**
     * 根据手机号获取游客身份司机
     *
     * @param phoneNumber
     * @return
     */
    @GetMapping("/getByPhoneNumber")
    BaseResponse<SocialDriverRegisterPurposeDto> getByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber);

    /**
     * 根据id获取游客身份司机
     *
     * @param id
     * @return
     */
    @GetMapping("/socialDriverRegisterPurpose/{id}")
    BaseResponse<SocialDriverRegisterPurposeDto> getSocialDriverRegisterPurposeDtoById(@PathVariable(value = "id") Integer id);

    /**
     * 根据手机号获取游客身份客户
     *
     * @param phoneNumber
     * @return
     */
    @GetMapping("/getCustomerRegisterPurposeByPhoneNumber")
    BaseResponse<CustomerRegisterPurposeDto> getCustomerRegisterPurposeByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber);

    /**
     * 根据id获取游客身份客户
     *
     * @param id
     * @return
     */
    @GetMapping("/customerRegisterPurpose/{id}")
    BaseResponse<CustomerRegisterPurposeDto> getCustomerRegisterPurposeDtoById(@PathVariable(value = "id") Integer id);

    /**
     * 从crm项目获取显示客户对象
     *
     * @param id
     * @return
     */
    @GetMapping("/getShowCustomer/{id}")
    ShowCustomerDto getShowCustomerById(@PathVariable("id") Integer id);

    /**
     * 获取装卸货地显示对象
     *
     * @param id
     * @return
     */
    @GetMapping("/getShowSite/{id}")
    ShowSiteDto getShowSiteById(@PathVariable("id") Integer id);

    /**
     * 获取当前tenant对象详情
     * @param id
     * @return
     */
    @GetMapping("/getTenantById/{id}")
    BaseResponse<GetTenantDto> getTenantById(@PathVariable("id") int id);
}
