package com.jaagro.user.biz.mapper;

import com.jaagro.user.biz.entity.Truck;

public interface TruckMapper {
    /**
     *
     * @mbggenerated 2018-08-22
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-22
     */
    int insert(Truck record);

    /**
     *
     * @mbggenerated 2018-08-22
     */
    int insertSelective(Truck record);

    /**
     *
     * @mbggenerated 2018-08-22
     */
    Truck selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-22
     */
    int updateByPrimaryKeySelective(Truck record);

    /**
     *
     * @mbggenerated 2018-08-22
     */
    int updateByPrimaryKey(Truck record);

}