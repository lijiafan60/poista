package com.ljf.ploughthewaves.interfaces;

import com.ljf.ploughthewaves.domain.wx.model.BehaviorMatter;
import com.ljf.ploughthewaves.domain.wx.model.MessageTextEntity;
import com.ljf.ploughthewaves.domain.wx.service.top.IWxReceiveService;
import com.ljf.ploughthewaves.domain.wx.service.top.IWxValidateService;
import com.ljf.ploughthewaves.domain.wx.utils.XmlUtil;
import com.ljf.ploughthewaves.infrastructure.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Resource;
import java.util.Date;

/**
 * 公众号入口
 */
@RestController
@RequestMapping("/wx/portal/{appid}")
public class WxPortalController {

    private Logger logger = LoggerFactory.getLogger(WxPortalController.class);

    @Resource
    private IWxValidateService wxValidateService;
    @Resource
    private IWxReceiveService wxReceiveService;
    @Resource
    private RedisUtil redisUtil;
    /**
     * 处理微信服务器发来的get请求，进行签名的验证
     * <p>
     * appid     微信端AppID
     * signature 微信端发来的签名
     * timestamp 微信端发来的时间戳
     * nonce     微信端发来的随机字符串
     * echostr   微信端发来的验证字符串
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String validate(@PathVariable String appid,
                           @RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce,
                           @RequestParam(value = "echostr", required = false) String echostr) {
        try {
            logger.info("微信公众号验签信息{}开始 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr);
            if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
                throw new IllegalArgumentException("请求参数非法，请核实!");
            }
            boolean check = wxValidateService.checkSign(signature, timestamp, nonce);
            logger.info("微信公众号验签信息{}完成 check：{}", appid, check);
            if (!check) {
                return null;
            }
            return echostr;
        } catch (Exception e) {
            logger.error("微信公众号验签信息{}失败 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr, e);
            return null;
        }
    }

    /**
     * 处理微信服务器的消息转发
     * @param appid
     * @param requestBody
     * @param signature
     * @param timestamp
     * @param nonce
     * @param openid
     * @param encType
     * @param msgSignature
     * @return
     */
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@PathVariable String appid,
                       @RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        try {
            logger.info("接收微信公众号信息请求{}开始 {}", openid, requestBody);
            MessageTextEntity message = XmlUtil.xmlToBean(requestBody, MessageTextEntity.class);
            if(redisUtil.get(message.getMsgId()) == null) {
                BehaviorMatter behaviorMatter = new BehaviorMatter();
                behaviorMatter.setOpenId(openid);
                behaviorMatter.setFromUserName(message.getFromUserName());
                behaviorMatter.setMsgType(message.getMsgType());
                behaviorMatter.setContent(StringUtils.isBlank(message.getContent()) ? null : message.getContent().trim());
                behaviorMatter.setEvent(message.getEvent());
                behaviorMatter.setCreateTime(new Date(Long.parseLong(message.getCreateTime()) * 1000L));
                // 处理消息
                String result = wxReceiveService.doReceive(behaviorMatter);
                redisUtil.set(message.getMsgId(),"1",5);
                logger.info("接收微信公众号信息请求{}完成 {}", openid, result);
                return result;
            } else {
                logger.info("微信公众号请求重复:MsgId:{},Content:{}",message.getMsgId(),requestBody);
                return "";
            }
        } catch (Exception e) {
            logger.error("接收微信公众号信息请求{}失败 {}", openid, requestBody, e);
            return "";
        }
    }

}
