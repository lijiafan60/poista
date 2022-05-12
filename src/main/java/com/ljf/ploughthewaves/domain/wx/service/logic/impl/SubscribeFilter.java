package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import org.springframework.stereotype.Service;

/**
 * 关注公众号逻辑
 */
@Service
public class SubscribeFilter implements LogicFilter {

    @Override
    public String filter(BehaviorMatter request) {
        return "感谢关注！期待与你的共同进步！" +
                "2 查看统计情况" +
                "3 更新统计情况" +
                "0 xx xx 绑定oj" +
                "1 xx xx 解绑oj" +
                "4 xxx xxx 设置姓名学校" ;
    }

}
