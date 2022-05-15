package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 更新信息逻辑
 */
@Service
@Slf4j
public class UpdStatisticsInfoFilter implements LogicFilter {

    @Override
    public String filter(BehaviorMatter request) {
        log.info("{}正在更新统计情况",request.getOpenId());
        //todo
        //mq

        return "正在更新您的统计数据";
    }

}
