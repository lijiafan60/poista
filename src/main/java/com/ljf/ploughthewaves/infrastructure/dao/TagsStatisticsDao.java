package com.ljf.ploughthewaves.infrastructure.dao;

import com.ljf.ploughthewaves.infrastructure.po.TagsStatistics;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagsStatisticsDao {

    void insert(TagsStatistics tagsStatistics);

    void update(TagsStatistics tagsStatistics);

    void deleteByUid(Integer uid);

    TagsStatistics queryByUid(Integer uid);
}
