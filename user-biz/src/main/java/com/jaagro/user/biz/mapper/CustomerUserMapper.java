package com.jaagro.user.biz.mapper;

import com.jaagro.user.biz.entity.CustomerUser;

public interface CustomerUserMapper {
    /**
     *
     * @mbggenerated 2019-02-26
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int insert(CustomerUser record);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int insertSelective(CustomerUser record);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    CustomerUser selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int updateByPrimaryKeySelective(CustomerUser record);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int updateByPrimaryKey(CustomerUser record);
}