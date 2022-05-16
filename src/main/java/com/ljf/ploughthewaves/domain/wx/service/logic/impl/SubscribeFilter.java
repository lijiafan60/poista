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
        return "感谢关注！欢迎使用统计平台!\n" +
                "你可以输入以下指令: \n" +
                "绑定OJ:0 OJ名 用户名\n" +
                "解绑OJ:1 OJ名 用户名\n" +
                "查看统计情情况:2\n" +
                "更新统计情况:3\n" +
                "设置姓名学校:4 姓名 学校\n" +
                "查看所有绑定的oj信息:5\n" +
                "tips: \n" +
                "1.后台将以学校为分组，对用户进行分组统计\n" +
                "2.每个分组可以有一个管理员能查看分组内的所有人的数据\n" +
                "3.如果你不想你的数据被别人看到，可以不设置姓名学校，或者把学校设置为null\n" +
                "4.一条指令的多个参数之间空一个空格，注意不要多输空格\n" +
                "5.如果你的指令没问题，会得到相应的回复，反之，如果后台检测到指令非法，将不会有任何回复";

    }

}
