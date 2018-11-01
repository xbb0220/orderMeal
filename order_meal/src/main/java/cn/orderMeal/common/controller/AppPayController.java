package cn.orderMeal.common.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import com.jfinal.ext.kit.DateKit;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.PaymentException;
import com.jfinal.wxaapp.WxaConfigKit;
import com.jfinal.wxaapp.api.WxaOrder;
import com.jfinal.wxaapp.api.WxaPayApi;
import cn.orderMeal.common.cons.SessionConst;
import cn.orderMeal.common.kit.GenerateOrderNoKit;
import cn.orderMeal.common.kit.session.SessionKit;
import cn.orderMeal.common.model.Order;
import cn.orderMeal.common.service.OrderService;
import cn.orderMeal.common.service.impl.OrderServiceImpl;

public class AppPayController extends BaseController{
	OrderService orderService = OrderServiceImpl.service;
	
	public void order() throws PaymentException {
		Calendar orderTime = Calendar.getInstance();
		String orderId = getPara("orderId");
    	Order orderById = orderService.getOrderById(orderId);
    	orderById.setOutTradeNo(GenerateOrderNoKit.gen("R",530L));
    	orderService.update(orderById);
    	WxaOrder order = new WxaOrder(WxaConfigKit.getWxaConfig().getAppId(), PropKit.get("mch_id"), PropKit.get("signKey"));
    	order.setOutTradeNo(orderById.getOutTradeNo());
    	order.setTotalFee(String.valueOf(orderById.getBigDecimal("totalPrice").multiply(new BigDecimal(100)).intValue()));
    	order.setTimeStart(DateKit.toStr(orderTime.getTime(), "yyyyMMddHHmmss"));
    	orderTime.add(Calendar.MINUTE, 30);
    	order.setTimeExpire(DateKit.toStr(orderTime.getTime(), "yyyyMMddHHmmss"));
    	order.setOpenId((String)SessionKit.getAttr(SessionConst.OPENID));
    	order.setNotifyUrl("notifyUrl");
    	Map<String, String> unifiedOrder = new WxaPayApi().unifiedOrder(order);
    	renderJson(unifiedOrder);
	}
	
	 /**
     * 支付成功通知
     */
    public void pay_notify() {
        // 支付结果通用通知文档: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
        String xmlMsg = HttpKit.readData(getRequest());
        System.out.println("支付通知="+xmlMsg);
        Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);

        String result_code  = params.get("result_code");
        // 总金额
        String totalFee     = params.get("total_fee");
        // 商户订单号
        String orderId      = params.get("out_trade_no");
        // 微信支付订单号
        String transId      = params.get("transaction_id");
        // 支付完成时间，格式为yyyyMMddHHmmss
        String timeEnd      = params.get("time_end");

        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        // 避免已经成功、关闭、退款的订单被再次更新

        if(PaymentKit.verifyNotify(params, PropKit.get("signKey"))){
            if (("SUCCESS").equals(result_code)) {
                //更新订单信息
                System.out.println("更新订单信息");

                Map<String, String> xml = new HashMap<String, String>();
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                renderText(PaymentKit.toXml(xml));
                return;
            }
        }
        renderText("");
    }
	
}
