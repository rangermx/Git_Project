package com.test.dao;

import java.util.ArrayList;

import com.test.domain.Group;

public interface GroupMapper {
    int deleteByPrimaryKey(Integer groupid);

    int insert(Group record);

    int insertSelective(Group record);

    Group selectByPrimaryKey(Integer groupid);
    
    ArrayList<Group> selectByUserid(Integer userid);

    int updateByPrimaryKeySelective(Group record);

    int updateByPrimaryKey(Group record);
}