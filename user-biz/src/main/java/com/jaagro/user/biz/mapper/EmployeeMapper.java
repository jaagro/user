package com.jaagro.user.biz.mapper;

import com.jaagro.user.biz.entity.Employee;

public interface EmployeeMapper {
    /**
     *
     * @mbggenerated 2019-02-26
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int insert(Employee record);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int insertSelective(Employee record);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    Employee selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int updateByPrimaryKeySelective(Employee record);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int updateByPrimaryKey(Employee record);
}