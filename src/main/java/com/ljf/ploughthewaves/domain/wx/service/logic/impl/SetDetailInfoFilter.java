package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.repository.IWxRepository;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 设置姓名，学校，是否公开逻辑
 */
@Service
@Slf4j
public class SetDetailInfoFilter implements LogicFilter {
    @Resource
    private IWxRepository wxRepository;
    @Override
    public String filter(BehaviorMatter request) {
        String params[] = request.getContent().split(" ");
        if(params.length != 3) {
            log.error("{}设置信息[参数非法]",request.getOpenId());
            return null;
        }
        log.info("{}正在设置信息: {} + {}",request.getOpenId(),params[1],params[2]);
        wxRepository.setDetailInfo(request.getOpenId(),params[1],params[2]);
        log.info("{}设置信息成功",request.getOpenId());
        return "设置成功";
    }
}
