package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.service.OrderService;
import cn.itcast.core.service.PayService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private RedisTemplate redisTemplate;
    @Reference
    private PayService payService;
    @Reference
    private OrderService orderService;

    /**
     * 生成支付链接
     *
     * @return
     */
    @RequestMapping("/createNative")
    public Map createNative() {
        //获取当前登录用户用户名
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据当前用户名到redis中获取支付日志对象
        PayLog payLog = payService.findPageLogFromRedis(userName);
        //根据支付日志的支付单号和总价格调用微信生成支付链接接口,生成支付链接返回
        if (payLog != null) {
            // Map map = payService.createNative(payLog.getOutTradeNo(), String.valueOf(payLog.getTotalFee()));
            Map map = payService.createNative(payLog.getOutTradeNo(), "1");
            return map;
        } else {
            return new HashMap();
        }

    }

    /**
     * 根据支付单号,查询是否支付成功
     *
     * @param out_trade_no 支付单号
     * @return
     */
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) throws Exception {
        Result result = null;

        Integer fiag=1;
        //死循环查询是否支付成功
        while (true) {
            //根据支付单号  调用查询接口 查询是否支付成功
            Map map = payService.queryPayStatus(out_trade_no);
            if (map == null) {
                result = new Result(false, "二维码超时");
            }
            //如果支付成功 修改支付日志为已经支付 修改订单为已支付  redis中的待支付日志对象也删除
            if ("SUCCESS".equals(map.get("trade_state"))) {
                 orderService.updatePayLogAndOrderStatus(out_trade_no);
                //返回支付成功的信息
                result = new Result(true, "支付成功!");
                break;
            }


            //每次查询睡三秒
            Thread.sleep(3000);
            fiag++;
            //半个小时不支付二维码失效重新生成二维码  继续扫描
            if (fiag>=600){
                result = new Result(true, "支付成功!");
                break;
            }
        }
        return result;
    }
}
