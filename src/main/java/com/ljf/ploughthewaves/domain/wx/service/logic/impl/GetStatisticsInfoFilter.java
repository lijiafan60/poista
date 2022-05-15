package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.repository.IWxRepository;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import com.ljf.ploughthewaves.domain.wx.utils.XmlUtil2;
import com.ljf.ploughthewaves.infrastructure.po.BindInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 获取所有OJ的刷题数逻辑
 */
@Service
@Slf4j
public class GetStatisticsInfoFilter implements LogicFilter {
    @Resource
    private IWxRepository wxRepository;
    @Override
    public String filter(BehaviorMatter request) {
        log.info("{}正在获取统计情况",request.getOpenId());
        List<BindInfo> resList = wxRepository.getAllBindInfo(request.getOpenId());
        log.info("{}成功获取统计情况",request.getOpenId());
        final StringBuffer res = new StringBuffer();
        resList.stream().forEach(x -> res.append(x.getOjName() + ": " + x.getOjUsername() + " " + x.getSolvedNumber() + "\n"));
        return res.toString();
    }
}
