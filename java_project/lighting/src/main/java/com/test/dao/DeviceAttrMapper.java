package com.test.dao;

import com.test.domain.DeviceAttr;

public interface DeviceAttrMapper {
    int deleteByPrimaryKey(String devMac);

    int insert(DeviceAttr record);

    int insertSelective(DeviceAttr record);

    DeviceAttr selectByPrimaryKey(String devMac);

    int updateByPrimaryKeySelective(DeviceAttr record);

    int updateByPrimaryKey(DeviceAttr record);
}