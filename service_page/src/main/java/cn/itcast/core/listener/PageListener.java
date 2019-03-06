package cn.itcast.core.listener;

import cn.itcast.core.service.CmsService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Map;

public class PageListener implements MessageListener {

    @Autowired
    private CmsService cmsService;
    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
        try {
            String goodsId = activeMQTextMessage.getText();
            //根据商品id查询商品的详细信息
            Map<String, Object> rootMap = cmsService.findGoods(Long.parseLong(goodsId));
            //生成静态页面
            cmsService.createStaticPage(Long.parseLong(goodsId),rootMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
