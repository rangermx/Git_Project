package com.test.dao;

import java.util.ArrayList;

import com.test.domain.GroupPair;

public interface GroupPairMapper {
    int deleteByPrimaryKey(Integer id);
    
    int deleteByGroupid(Integer groupid);

    int insert(GroupPair record);

    int insertSelective(GroupPair record);

    GroupPair selectByPrimaryKey(Integer id);
    
    ArrayList<GroupPair> selectByUserid(Integer userid);
    
    ArrayList<GroupPair> selectByZigbeeMac(String ZigbeeMac);

    int updateByPrimaryKeySelective(GroupPair record);

    int updateByPrimaryKey(GroupPair record);
}