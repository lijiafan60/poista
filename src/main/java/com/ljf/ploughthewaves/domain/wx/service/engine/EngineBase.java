package com.ljf.ploughthewaves.domain.wx.service.engine;


import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 引擎基类
 */
@Slf4j
public class EngineBase extends EngineConfig implements Engine {

    @Override
    public String process(BehaviorMatter request) {
        return null;
    }

    /**
     * 消息类型&业务内容路由器
     *
     * @param request 消息内容
     * @return 返回消息处理器
     */
    protected LogicFilter router(BehaviorMatter request) {

        Map<String, LogicFilter> logicGroup = logicFilterMap.get(request.getMsgType());

        // 事件处理
        if ("event".equals(request.getMsgType())) {
            return logicGroup.get(request.getEvent());
        }

        // 内容处理
        if ("text".equals(request.getMsgType())) {
            if(Pattern.matches("^[0-4].*",request.getContent())) {
                return logicGroup.get(request.getContent().substring(0,1));
            }
            return null;
        }
        return null;
    }
}
