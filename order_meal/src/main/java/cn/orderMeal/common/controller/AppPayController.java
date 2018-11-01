package cn.orderMeal.common.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.PaymentException;
import com.jfinal.wxaapp.WxaConfigKit;
import com.jfinal.wxaapp.api.WxaOrder;
import com.jfinal.wxaapp.api.WxaPayApi;
import cn.orderMeal.common.anum.OrderStatus;
import cn.orderMeal.common.kit.AjaxJson;
import cn.orderMeal.common.kit.DateKit;
import cn.orderMeal.common.model.Order;
import cn.orderMeal.common.service.OrderService;
import cn.orderMeal.common.service.impl.OrderServiceImpl;

public class AppPayController extends BaseController {
	OrderService orderService = OrderServiceImpl.service;

	@Before(Tx.class)
	public void order() throws PaymentException {
		String orderId = getPara("orderId");
		Order orderById = orderService.getOrderById(orderId);
		if (!OrderStatus.WAITING_PAY.getCode().equals(orderById.getStatus())) {
			renderJson(AjaxJson.failure().setMsg("该订单不支持支付操作"));
			return;
		}
		WxaOrder order = new WxaOrder(WxaConfigKit.getWxaConfig().getAppId(), PropKit.get("mch_id"), PropKit.get("signKey"));
		order.setTotalFee(String.valueOf(orderById.getTotalFee()));
		order.setTimeStart(DateKit.formatDate(orderById.getTimeStart(), "yyyyMMddHHmmss"));
		order.setTimeExpire(DateKit.formatDate(orderById.getTimeExpire(), "yyyyMMddHHmmss"));
		order.setOpenId(orderById.getOpenId());
		order.setNotifyUrl("notifyUrl");
		Map<String, String> unifiedOrder = new WxaPayApi().unifiedOrder(order);
		renderJson(AjaxJson.success().setData(unifiedOrder));
	}

	/**
	 * 支付成功通知
	 * @throws ParseException 
	 */
	public void pay_notify() throws ParseException {
		// 支付结果通用通知文档: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
		String xmlMsg = HttpKit.readData(getRequest());
		System.out.println("支付通知=" + xmlMsg);
		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);

		String result_code = params.get("result_code");
		// 总金额
		String totalFee = params.get("total_fee");
		// 商户订单号
		String orderId = params.get("out_trade_no");
		// 微信支付订单号
		String transId = params.get("transaction_id");
		// 支付完成时间，格式为yyyyMMddHHmmss
		String timeEnd = params.get("time_end");

		// 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
		// 避免已经成功、关闭、退款的订单被再次更新

		if (PaymentKit.verifyNotify(params, PropKit.get("signKey"))) {
			if (("SUCCESS").equals(result_code)) {
				// 更新订单信息
				System.out.println("更新订单信息");
				Order order = new Order().setOutTradeNo(orderId);
				Order dbOrder = orderService.getByEqualAttr(order, "outTradeNo");
				dbOrder.setTimeEnd(DateKit.stringToDate(timeEnd, "yyyyMMddHHmmss"));
				dbOrder.setStatus(OrderStatus.FINISH.getCode());
				dbOrder.setTransId(transId);
				orderService.update(dbOrder);
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
