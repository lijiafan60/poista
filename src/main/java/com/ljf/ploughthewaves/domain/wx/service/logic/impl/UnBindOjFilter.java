package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 删除oj信息的处理逻辑
 */
@Service
@Slf4j
public class UnBindOjFilter implements LogicFilter {
    @Override
    public String filter(BehaviorMatter request) {
        String params[] = request.getContent().split(" ");
        if(params.length != 3) {
            return null;
        }
        log.info("{}正在解绑OJ: {} + {}",request.getOpenId(),params[1],params[2]);
        return null;
    }
}
