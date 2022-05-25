package com.ljf.ploughthewaves.domain.wx.service.logic.impl;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.repository.IWxRepository;
import com.ljf.ploughthewaves.domain.wx.service.logic.LogicFilter;
import com.ljf.ploughthewaves.infrastructure.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 设置姓名，学校，是否公开逻辑
 */
@Service
@Slf4j
public class SetDetailInfoFilter implements LogicFilter {
    @Resource
    private IWxRepository wxRepository;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public String filter(BehaviorMatter request) {
        String params[] = request.getContent().split(" ");
        if(params.length != 3) {
            log.error("{}设置信息[参数非法]",request.getOpenId());
            return null;
        }
        log.info("{}正在设置信息: {} + {}",request.getOpenId(),params[1],params[2]);
        Integer code = wxRepository.setDetailInfo(request.getOpenId(),params[1],params[2]);
        if(code == 1) {
            log.info("{}设置信息成功", request.getOpenId());
            return "设置成功";
        } else if(code == -1) {
            log.info("{}设置失败,该用户名已存在",request.getOpenId());
            return "设置失败,用户名已存在";
        } else {
            log.error("{}设置失败，原因未知！！",request.getOpenId());
            return "设置失败";
        }
    }
}
