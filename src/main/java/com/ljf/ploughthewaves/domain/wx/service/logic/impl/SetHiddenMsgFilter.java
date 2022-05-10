package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import org.springframework.stereotype.Service;

/**
 * 设置姓名，学校，是否公开逻辑
 */
@Service
public class SetHiddenMsgFilter implements LogicFilter {
    @Override
    public String filter(BehaviorMatter request) {
        return null;
    }
}
