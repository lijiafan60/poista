package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.repository.IWxRepository;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import com.ljf.ploughthewaves.infrastructure.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 更新信息逻辑
 */
@Service
@Slf4j
public class UpdStatisticsInfoFilter implements LogicFilter {

    @Resource
    private IWxRepository wxRepository;

    @Override
    public String filter(BehaviorMatter request) {
        log.info("{}正在更新统计情况",request.getOpenId());
        wxRepository.updateStatisticsInfo(request.getOpenId());
        return "正在快马加鞭更新中......请稍等10s后进行查看";
    }

}
