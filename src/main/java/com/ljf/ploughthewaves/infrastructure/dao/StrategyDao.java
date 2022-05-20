package com.ljf.ploughthewaves.infrastructure.dao;

import com.ljf.ploughthewaves.infrastructure.po.Strategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StrategyDao {

    void addStrategy(@Param("strategy") Strategy strategy, @Param("school") String school);

    void delStrategy(String school);

    Strategy getStrategy(String school);
}
