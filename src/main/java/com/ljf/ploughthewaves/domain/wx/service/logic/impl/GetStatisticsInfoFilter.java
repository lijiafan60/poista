package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 获取所有OJ的刷题数逻辑
 */
@Service
@Slf4j
public class GetStatisticsInfoFilter implements LogicFilter {
    @Override
    public String filter(BehaviorMatter request) {
        log.info("{}正在获取统计情况",request.getOpenId());
        return null;
    }
}
