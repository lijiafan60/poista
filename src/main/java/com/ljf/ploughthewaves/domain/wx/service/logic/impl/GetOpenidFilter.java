package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import org.springframework.stereotype.Service;

@Service
public class GetOpenidFilter implements LogicFilter {

    @Override
    public String filter(BehaviorMatter request) {
        return request.getOpenId();
    }
}
