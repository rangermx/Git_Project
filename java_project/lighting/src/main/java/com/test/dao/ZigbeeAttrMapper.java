package com.test.dao;

import com.test.domain.ZigbeeAttr;

public interface ZigbeeAttrMapper {
    int deleteByPrimaryKey(String zigbeeMac);

    int insert(ZigbeeAttr record);

    int insertSelective(ZigbeeAttr record);

    ZigbeeAttr selectByPrimaryKey(String zigbeeMac);

    int updateByPrimaryKeySelective(ZigbeeAttr record);

    int updateByPrimaryKey(ZigbeeAttr record);
}