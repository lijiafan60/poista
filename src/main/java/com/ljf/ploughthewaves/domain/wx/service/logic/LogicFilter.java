package com.ljf.ploughthewaves.domain.wx.service.logic;


import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;

/**
 * 逻辑接口
 */
public interface LogicFilter {

    String filter(BehaviorMatter request);

}
