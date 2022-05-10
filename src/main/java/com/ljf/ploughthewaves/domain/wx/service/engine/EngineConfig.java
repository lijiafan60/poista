package com.ljf.ploughthewaves.domain.wx.service.engine;


import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import com.ljf.ploughthewaves.domain.wx.service.logic.impl.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 逻辑工厂
 */
public class EngineConfig {

    @Resource
    private GetAllOjSNFilter getAllOjSNFilter;

    @Resource
    private SetHiddenMsgFilter setHiddenMsgFilter;

    @Resource
    private SetOjInfoFilter setOjInfoFilter;

    @Resource
    private UpdSNFilter updSNFilter;

    @Resource
    private SubscribeFilter subscribeFilter;

    @Resource
    private UnsubscribeFilter unsubscribeFilter;

    protected static Map<String, Map<String, LogicFilter>> logicFilterMap = new HashMap<>();

    @PostConstruct
    public void init() {

        logicFilterMap.put("text", new HashMap<String, LogicFilter>() {
            {
                put("getAllOjSN",getAllOjSNFilter);
                put("setHiddenMsg",setHiddenMsgFilter);
                put("setOjInfo",setOjInfoFilter);
                put("updSN",updSNFilter);
            }
        });

        logicFilterMap.put("event", new HashMap<String, LogicFilter>() {
            {
                put("subscribe", subscribeFilter);
                put("unsubscribe", unsubscribeFilter);
            }
        });
    }

}
