package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.repository.IWxRepository;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GetAllBindInfo implements LogicFilter {

    @Resource
    private IWxRepository wxRepository;

    @Override
    public String filter(BehaviorMatter request) {
        log.info("{}查看所有绑定的信息",request.getOpenId());
        List<Map<String,String>> resList = wxRepository.getAllBindInfo(request.getOpenId());
        //todo
        return null;
    }
}
