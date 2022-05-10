package com.ljf.ploughthewaves.domain.wx.service.top.impl;

import com.ljf.ploughthewaves.domain.wx.service.top.IWxValidateService;
import com.ljf.ploughthewaves.domain.wx.utils.SignatureUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 公众号验证
 */
@Service
public class WxValidateServiceImpl implements IWxValidateService {

    @Value("${wx.config.token}")
    private String token;

    @Override
    public boolean checkSign(String signature, String timestamp, String nonce) {
        return SignatureUtil.check(token, signature, timestamp, nonce);
    }

}
