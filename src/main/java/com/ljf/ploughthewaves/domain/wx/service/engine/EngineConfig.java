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
    private UpdStatisticsInfoFilter updStatisticsInfoFilter;

    @Resource
    private GetStatisticsInfoFilter getStatisticsInfoFilter;

    @Resource
    private SetDetailInfoFilter setDetailInfoFilter;

    @Resource
    private BindOjFilter bindOjFilter;

    @Resource
    private UnBindOjFilter unBindOjFilter;

    @Resource
    private SubscribeFilter subscribeFilter;

    @Resource
    private UnsubscribeFilter unsubscribeFilter;

    @Resource
    private GetAllBindInfoFilter getAllBindInfoFilter;

    @Resource
    private GetOpenidFilter getOpenidFilter;

    protected static Map<String, Map<String, LogicFilter>> logicFilterMap = new HashMap<>();

    @PostConstruct
    public void init() {

        logicFilterMap.put("text", new HashMap<String, LogicFilter>() {
            {
                put("0",bindOjFilter);
                put("1",unBindOjFilter);
                put("2",getStatisticsInfoFilter);
                put("3",updStatisticsInfoFilter);
                put("4",setDetailInfoFilter);
                put("5", getAllBindInfoFilter);
                put("6",getOpenidFilter);
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
