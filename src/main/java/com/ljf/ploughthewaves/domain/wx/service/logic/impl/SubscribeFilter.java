package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.repository.IWxRepository;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 关注公众号逻辑
 */
@Service
public class SubscribeFilter implements LogicFilter {

    @Resource
    private IWxRepository wxRepository;

    @Override
    public String filter(BehaviorMatter request) {
        wxRepository.addUser(request.getOpenId());
        return "感谢关注！期待与你的共同进步！\n" +
                "2 查看统计情情况\n" +
                "3 更新统计情况\n" +
                "0 xx xx 绑定oj\n" +
                "1 xx xx 解绑oj\n" +
                "4 xxx xxx 设置姓名学校\n" +
                "5 查看所有绑定的oj信息\n" ;
    }

}
