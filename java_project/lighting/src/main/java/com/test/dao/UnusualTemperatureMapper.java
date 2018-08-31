package com.test.dao;

import com.test.domain.UnusualTemperature;

public interface UnusualTemperatureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UnusualTemperature record);

    int insertSelective(UnusualTemperature record);

    UnusualTemperature selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UnusualTemperature record);

    int updateByPrimaryKey(UnusualTemperature record);
}