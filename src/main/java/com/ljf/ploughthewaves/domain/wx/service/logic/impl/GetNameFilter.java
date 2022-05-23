package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.admin.service.UserService;
import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import com.ljf.ploughthewaves.infrastructure.dao.UserDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GetNameFilter implements LogicFilter {

    @Resource
    UserDao userDao;
    @Override
    public String filter(BehaviorMatter request) {
        return userDao.queryUserByOpenid(request.getOpenId()).getName();
    }
}
