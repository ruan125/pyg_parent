package cn.itcast.core.listener;

import cn.itcast.core.service.SolrManagerService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/*
 * 自定义监听器
 * 监听来自消息服务器发送来的消息  在这里接受消息后 ,对商品进行上架操作
 * */
public class ItemSearchListener implements MessageListener {
    @Autowired
    private SolrManagerService solrManagerService;

    @Override
    public void onMessage(Message message) {


        //1.接收消息
        ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
        try {

            String goodId = activeMQTextMessage.getText();
            //2.根据商品的id到数据库查询商品的详细数据,放入solr索引库供前台portal系统搜索使用
            solrManagerService.importItemToSolr(Long.parseLong(goodId));
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
