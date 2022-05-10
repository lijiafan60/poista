package com.ljf.ploughthewaves.domain.wx.service.engine;


import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;

/**
 * 消息引擎
 */
public interface Engine {

    /**
     * 过滤器
     * @param request       入参
     * @return              出参
     */
    String process(final BehaviorMatter request);

}
