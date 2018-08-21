package com.test.dao;

import java.util.ArrayList;

import com.test.domain.Zigbee;

public interface ZigbeeMapper {
    int deleteByPrimaryKey(String zigbeeMac);

    int insert(Zigbee record);

    int insertSelective(Zigbee record);

    Zigbee selectByPrimaryKey(String zigbeeMac);
    
    Zigbee selectBySAddrAndDevMac(String sAddr, String devMac);
    
    ArrayList<Zigbee> selectBydevMac(String devMac);

    int updateByPrimaryKeySelective(Zigbee record);

    int updateByPrimaryKey(Zigbee record);
    
    int updateBydevMacSelectiveWhereOnline(Zigbee record);
}