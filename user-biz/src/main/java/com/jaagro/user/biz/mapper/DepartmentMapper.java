package com.jaagro.user.biz.mapper;

import com.jaagro.user.biz.entity.Department;

public interface DepartmentMapper {
    /**
     *
     * @mbggenerated 2019-02-26
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int insert(Department record);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int insertSelective(Department record);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    Department selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int updateByPrimaryKeySelective(Department record);

    /**
     *
     * @mbggenerated 2019-02-26
     */
    int updateByPrimaryKey(Department record);
}