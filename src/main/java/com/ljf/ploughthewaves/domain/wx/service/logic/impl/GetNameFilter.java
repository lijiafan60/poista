package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.admin.service.UserService;
import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import com.ljf.ploughthewaves.infrastructure.po.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GetNameFilter implements LogicFilter {

    @Resource
    UserDao userDao;
    @Override
    public String filter(BehaviorMatter request) {
        User user = userDao.queryUserByOpenid(request.getOpenId());
        String res = "用户名：" + user.getName() + "; 密码：" ;
        if(user.getPassword() == null) {
            res += "你还没设置密码，请先前往网页端注册。";
        } else {
            res += user.getPassword();
        }
        return res;
    }
}
