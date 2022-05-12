package com.ljf.ploughthewaves.domain.wx.service.engine.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.model.MessageTextEntity;
import com.ljf.ploughthewaves.domain.wx.service.engine.EngineBase;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import com.ljf.ploughthewaves.domain.wx.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 消息处理
 */
@Service("msgEngineHandle")
@Slf4j
public class MsgEngineHandle extends EngineBase {

    @Value("${wx.config.originalid}")
    private String originalId;

    @Override
    public String process(BehaviorMatter request) {
        LogicFilter logicFilter = super.router(request);
        if (null == logicFilter) {
            return null;
        }
        String resultStr = logicFilter.filter(request);
        if (StringUtils.isBlank(resultStr)) {
            return "";
        }

        //反馈信息[文本]
        MessageTextEntity res = new MessageTextEntity();
        res.setToUserName(request.getOpenId());
        res.setFromUserName(originalId);
        res.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000L));
        res.setMsgType("text");
        res.setContent(resultStr);
        return XmlUtil.beanToXml(res);
    }

}
