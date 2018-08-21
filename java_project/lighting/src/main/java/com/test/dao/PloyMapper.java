package com.test.dao;

import java.util.ArrayList;

import com.test.domain.Ploy;

public interface PloyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Ploy record);

    int insertSelective(Ploy record);

    Ploy selectByPrimaryKey(Integer id);
    
    ArrayList<Ploy> selectByUserid(Integer userid);
    
    ArrayList<Ploy> selectByStatus(Integer status);

    int updateByPrimaryKeySelective(Ploy record);

    int updateByPrimaryKey(Ploy record);
}