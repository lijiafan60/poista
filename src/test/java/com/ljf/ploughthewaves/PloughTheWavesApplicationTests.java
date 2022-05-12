package com.ljf.ploughthewaves;

import com.ljf.ploughthewaves.domain.wx.model.MessageTextEntity;
import com.ljf.ploughthewaves.domain.wx.utils.XmlUtil;
import com.ljf.ploughthewaves.interfaces.WxPortalController;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PloughTheWavesApplicationTests {
    private Logger logger = LoggerFactory.getLogger(PloughTheWavesApplicationTests.class);
    @Test
    void contextLoads() {
    }
    @Test
    void testXmlUtil() {
        String requestBody = "<xml><ToUserName><![CDATA[gh_b4f81d822ae3]]></ToUserName><FromUserName><![CDATA[o0_qEt7Vy0ABFnjXpsm9TbKqlYlo]]></FromUserName><CreateTime>1652291116</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[h]]></Content><MsgId>23655123084782957</MsgId></xml>";
        logger.info("接收微信公众号信息请求{}开始 {}",1,requestBody);
        MessageTextEntity message = XmlUtil.xmlToBean(requestBody,MessageTextEntity.class);
        logger.info("xml转bean完成");
        System.out.println(message.getMsgType());
        System.out.println(message.getCreateTime());
    }
}
