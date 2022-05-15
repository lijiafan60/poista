package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.repository.IWxRepository;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 取消关注逻辑
 */
@Service
@Slf4j
public class UnsubscribeFilter implements LogicFilter {
    @Resource
    private IWxRepository wxRepository;

    @Override
    public String filter(BehaviorMatter request) {
        log.info("用户{}已取消关注", request.getOpenId());
        wxRepository.deleteUser(request.getOpenId());
        return null;
    }

}
