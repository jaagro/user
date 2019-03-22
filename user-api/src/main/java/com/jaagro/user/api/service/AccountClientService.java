package com.jaagro.user.api.service;

import com.jaagro.user.api.dto.request.account.BatchDeleteAccountDto;
import com.jaagro.user.api.dto.request.account.QueryAccountDto;
import com.jaagro.user.api.dto.response.account.AccountReturnDto;
import com.jaagro.utils.BaseResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 账户远程操作
 * @author yj
 * @date 2018/10/25
 */
@FeignClient(value = "account")
public interface AccountClientService {
    /**
     * 删除账户
     * @param id
     * @return
     */
    @DeleteMapping("/account/{id}")
    BaseResponse deleteAccount(@PathVariable("id") Integer id);

    /**
     * 查询账户
     *@param queryAccountDto
     * @return
     */
    @PostMapping("/getByQueryAccountDto")
    AccountReturnDto getByQueryAccountDto(@RequestBody QueryAccountDto queryAccountDto);

    /**
     * 逻辑批量删除账户
     *@param batchDeleteAccountDto
     * @return
     */
    @DeleteMapping("/batchDelete")
    BaseResponse batchDelete(@RequestBody BatchDeleteAccountDto batchDeleteAccountDto);
}