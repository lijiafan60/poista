package com.ljf.ploughthewaves.domain.wx.service.top.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.service.engine.Engine;
import com.ljf.ploughthewaves.domain.wx.service.top.IWxReceiveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 接收消息处理
 */
@Service
public class WxReceiveServiceImpl implements IWxReceiveService {

    @Resource(name = "msgEngineHandle")
    private Engine msgEngineHandle;

    @Override
    public String doReceive(BehaviorMatter behaviorMatter) {
        return msgEngineHandle.process(behaviorMatter);
    }

}
