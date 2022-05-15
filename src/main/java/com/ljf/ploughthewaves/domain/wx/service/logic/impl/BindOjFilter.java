package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.poista.service.util.OjFilter;
import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.repository.IWxRepository;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 设置oj信息的处理逻辑
 */
@Service
@Slf4j
public class BindOjFilter implements LogicFilter{
    @Resource
    private IWxRepository wxRepository;

    @Override
    public String filter(BehaviorMatter request) {
        String params[] = request.getContent().split(" ");
        if(params.length != 3) {
            return null;
        }
        log.info("{}正在绑定OJ: {} + {}",request.getOpenId(),params[1],params[2]);
        Integer ojType = OjFilter.NameToType.get(params[1]);
        log.info("绑定的ojType为：{}",ojType);
        if(null == ojType) {
            log.error("没找到oj : {}",params[1]);
            return null;
        }
        if(ojType.intValue() >= 2) wxRepository.addOj1(ojType,params[2],request.getOpenId());
        else wxRepository.addOj2(ojType,params[2],request.getOpenId());
        log.info("绑定成功");
        return "绑定成功";
    }
}
