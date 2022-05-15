package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.repository.IWxRepository;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import com.ljf.ploughthewaves.domain.wx.utils.XmlUtil2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        Map<String,String> map = wxRepository.getStatisticsInfo(request.getOpenId());
        String xmlRes = XmlUtil2.mapToXML(map);
        log.info("统计结果为：{}",xmlRes);
        return xmlRes;
    }
}
