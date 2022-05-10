package com.ljf.ploughthewaves.domain.wx.service.top;


import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;

public interface IWxReceiveService {

    /**
     * 接收信息
     *
     * @param behaviorMatter    入参
     * @return                  出惨
     * @throws Exception        异常
     */
    String doReceive(BehaviorMatter behaviorMatter) throws Exception;

}
