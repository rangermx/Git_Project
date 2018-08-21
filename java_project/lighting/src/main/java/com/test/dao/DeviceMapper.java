package com.test.dao;

import java.util.ArrayList;

import com.test.domain.Device;

public interface DeviceMapper {
    int deleteByPrimaryKey(String devMac);

    int insert(Device record);

    int insertSelective(Device record);

    Device selectByPrimaryKey(String devMac);
    
    ArrayList<Device> selectByUserid(Integer userid);

    int updateByPrimaryKeySelective(Device record);

    int updateByPrimaryKey(Device record);
}