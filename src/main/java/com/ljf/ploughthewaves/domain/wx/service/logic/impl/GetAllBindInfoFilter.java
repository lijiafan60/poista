package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.repository.IWxRepository;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import com.ljf.ploughthewaves.infrastructure.po.BindInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class GetAllBindInfoFilter implements LogicFilter {

    @Resource
    private IWxRepository wxRepository;

    @Override
    public String filter(BehaviorMatter request) {
        log.info("{}查看所有绑定的信息",request.getOpenId());
        List<BindInfo> resList = wxRepository.getAllBindInfo(request.getOpenId());
        log.info("{}查看绑定信息成功",request.getOpenId());
        final StringBuffer res = new StringBuffer();
        resList.stream().forEach(x -> res.append(x.getOjName() + ": " + x.getOjUsername() +"\n"));
        return res.toString();
    }
}
