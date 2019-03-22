package com.jaagro.user.biz.mapper;

import com.jaagro.user.biz.entity.Driver;

public interface DriverMapper {
    /**
     *
     * @mbggenerated 2019-02-26
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int insert(Driver record);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int insertSelective(Driver record);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    Driver selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int updateByPrimaryKeySelective(Driver record);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int updateByPrimaryKey(Driver record);
}