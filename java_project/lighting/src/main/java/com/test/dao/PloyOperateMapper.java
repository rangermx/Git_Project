package com.test.dao;

import java.util.ArrayList;

import com.test.domain.PloyOperate;

public interface PloyOperateMapper {
    int deleteByPrimaryKey(Integer id);
    
    int deleteByPloyid(Integer ployid);

    int insert(PloyOperate record);

    int insertSelective(PloyOperate record);

    PloyOperate selectByPrimaryKey(Integer id);
    
    ArrayList<PloyOperate> selectByPloyid(Integer ployid);

    int updateByPrimaryKeySelective(PloyOperate record);

    int updateByPrimaryKey(PloyOperate record);
}