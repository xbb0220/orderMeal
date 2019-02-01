package cn.orderMeal.common.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants.TradeType;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.jfinal.aop.Before;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.weixin.sdk.kit.IpKit;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.PaymentException;
import cn.orderMeal.common.anum.OrderStatus;
import cn.orderMeal.common.config.wx.miniapp.WxPayConfiguration;
import cn.orderMeal.common.kit.AjaxJson;
import cn.orderMeal.common.kit.DateKit;
import cn.orderMeal.common.model.Order;
import cn.orderMeal.common.model.OrderItem;
import cn.orderMeal.common.service.OrderItemService;
import cn.orderMeal.common.service.OrderService;
import cn.orderMeal.common.service.impl.OrderItemServiceImpl;
import cn.orderMeal.common.service.impl.OrderServiceImpl;
import cn.orderMeal.common.vo.Good;
import cn.orderMeal.common.vo.PrintVo;

public class AppPayController extends BaseController {
	OrderService orderService = OrderServiceImpl.service;
	OrderItemService orderItemService = OrderItemServiceImpl.service;
	/**
	 * 微信统一下单
	 * @throws PaymentException
	 */
//	@Before(Tx.class)
//	public void order() throws PaymentException {
//		String orderId = getPara("orderId");
//		Order orderById = orderService.getOrderById(orderId);
//		if (!OrderStatus.WAITING_PAY.getCode().equals(orderById.getStatus())) {
//			renderJson(AjaxJson.failure().setMsg("该订单不支持支付操作"));
//			return;
//		}
//		if (orderById.getTimeExpire().before(new Date())) {
//			orderById.setStatus(OrderStatus.CANCEL.getCode()).update();
//			renderJson(AjaxJson.failure().setMsg("您已超过半小时未支付，请重新下单"));
//			return;
//		}
//		WxaOrder order = new WxaOrder(WxaConfigKit.getWxaConfig().getAppId(), PropKit.get("mch_id"), PropKit.get("signKey"));
//		order.setTotalFee(String.valueOf(orderById.getTotalFee()));
//		order.setTimeStart(DateKit.formatDate(orderById.getTimeStart(), "yyyyMMddHHmmss"));
//		order.setTimeExpire(DateKit.formatDate(orderById.getTimeExpire(), "yyyyMMddHHmmss"));
//		order.setOpenId(orderById.getOpenId());
//		order.setNotifyUrl(PropKit.get("payNotifyUrl"));
//		order.setOutTradeNo(orderById.getOutTradeNo());
//		String ip = IpKit.getRealIp(getRequest());
//		if (StrKit.isBlank(ip)) {
//			ip = "127.0.0.1";
//		}
//		order.setSpbillCreateIp(ip);
//		order.setBody("下单");
//		Map<String, String> unifiedOrder = new WxaPayApi().unifiedOrder(order);
//		renderJson(AjaxJson.success().setData(unifiedOrder));
//	}

	
	/**
	 * 微信统一下单
	 * @throws PaymentException
	 * @throws WxPayException 
	 */
	@Before(Tx.class)
	public void order() throws PaymentException, WxPayException {
		String orderId = getPara("orderId");
		Order orderById = orderService.getOrderById(orderId);
		if (!OrderStatus.WAITING_PAY.getCode().equals(orderById.getStatus())) {
			renderJson(AjaxJson.failure().setMsg("该订单不支持支付操作"));
			return;
		}
		if (orderById.getTimeExpire().before(new Date())) {
			orderById.setStatus(OrderStatus.CANCEL.getCode()).update();
			renderJson(AjaxJson.failure().setMsg("您已超过半小时未支付，请重新下单"));
			return;
		}
		WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
		request.setTotalFee(orderById.getTotalFee());
		request.setTimeStart(DateKit.formatDate(orderById.getTimeStart(), "yyyyMMddHHmmss"));
		request.setTimeExpire(DateKit.formatDate(orderById.getTimeExpire(), "yyyyMMddHHmmss"));
		request.setOpenid(orderById.getOpenId());
		String ip = IpKit.getRealIp(getRequest());
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}
		request.setSpbillCreateIp(ip);
		request.setBody("下单");
		
		request.setNotifyUrl(PropKit.get("payNotifyUrl"));
		request.setOutTradeNo(orderById.getOutTradeNo());
		request.setTradeType(TradeType.JSAPI);
		WxPayMpOrderResult unifiedOrder = WxPayConfiguration.wxPayService.createOrder(request);
		renderJson(AjaxJson.success().setData(unifiedOrder));
	}
	
	public void paying() {
		Order order = orderService.findByIdLoadColumns(getPara("id"), "id,status");
		if (!OrderStatus.WAITING_PAY.getCode().equals(order.getStatus())) {
			renderJson(AjaxJson.failure().setMsg("订单当前状态不支持访问"));
			return;
		}
		order.setStatus(OrderStatus.PAYING.getCode());
		orderService.update(order);
		renderJson(AjaxJson.success());
	}
	
	
	
	/**
	 * 支付成功通知
	 * @throws ParseException 
	 * @throws WxPayException 
	 */
	public void pay_notify() throws ParseException, WxPayException {
		String xmlMsg = HttpKit.readData(getRequest());
		final WxPayOrderNotifyResult notifyResult = WxPayConfiguration.wxPayService.parseOrderNotifyResult(xmlMsg);
	    // TODO 根据自己业务场景需要构造返回对象
	    //return WxPayNotifyResponse.success("成功");
		// 支付结果通用通知文档: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
		System.out.println("支付通知=" + xmlMsg);
		//String result_code = notifyResult.getResultCode();
		// 总金额
		Integer totalFee = notifyResult.getTotalFee();
		// 商户订单号
		String orderId = notifyResult.getOutTradeNo();
		// 微信支付订单号
		String transId = notifyResult.getTransactionId();
		// 支付完成时间，格式为yyyyMMddHHmmss
		String timeEnd = notifyResult.getTimeEnd();
		// 更新订单信息
		System.out.println("更新订单信息");
		Order dbOrder = orderService.findFirst(" select * from `order` where 1=1  and outTradeNo =? ", orderId);;
		dbOrder.setTimeEnd(DateKit.stringToDate(timeEnd, "yyyyMMddHHmmss"));
		dbOrder.setStatus(OrderStatus.FINISH.getCode());
		dbOrder.setTransId(transId);
		dbOrder.setFinalTotalFee(Integer.valueOf(totalFee));
		orderService.update(dbOrder);
		print(dbOrder.getId()); 
		Map<String, String> xml = new HashMap<String, String>();
		xml.put("return_code", "SUCCESS");
		xml.put("return_msg", "OK");
		renderText(PaymentKit.toXml(xml));
	}
	
	
	/**
	 * 支付成功通知
	 * @throws ParseException 
	 */
//	public void pay_notify() throws ParseException {
//		// 支付结果通用通知文档: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
//		String xmlMsg = HttpKit.readData(getRequest());
//		System.out.println("支付通知=" + xmlMsg);
//		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
//
//		String result_code = params.get("result_code");
//		// 总金额
//		String totalFee = params.get("total_fee");
//		// 商户订单号
//		String orderId = params.get("out_trade_no");
//		// 微信支付订单号
//		String transId = params.get("transaction_id");
//		// 支付完成时间，格式为yyyyMMddHHmmss
//		String timeEnd = params.get("time_end");
//
//		// 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
//		// 避免已经成功、关闭、退款的订单被再次更新
//
//		if (PaymentKit.verifyNotify(params, PropKit.get("signKey"))) {
//			if (("SUCCESS").equals(result_code)) {
//				// 更新订单信息
//				System.out.println("更新订单信息");
//				Order dbOrder = orderService.findFirst(" select * from `order` where 1=1  and outTradeNo =? ", orderId);;
//				dbOrder.setTimeEnd(DateKit.stringToDate(timeEnd, "yyyyMMddHHmmss"));
//				dbOrder.setStatus(OrderStatus.FINISH.getCode());
//				dbOrder.setTransId(transId);
//				dbOrder.setFinalTotalFee(Integer.valueOf(totalFee));
//				orderService.update(dbOrder);
//				print(dbOrder.getId()); 
//				Map<String, String> xml = new HashMap<String, String>();
//				xml.put("return_code", "SUCCESS");
//				xml.put("return_msg", "OK");
//				renderText(PaymentKit.toXml(xml));
//				return;
//			}
//		}
//		renderText("");
//	}

	public void print(String id) {
		Order order = orderService.getOrderById(id);
		OrderItem oi = new OrderItem().setOrderId(order.getId());
		List<OrderItem> orderItems = orderItemService.getAllByEqualAttr(oi, "orderId");
		PrintVo printVo = new PrintVo();
		printVo.setCreateTime(order.getCreateTime());
		printVo.setTotalOrder(order.getBigDecimal("totalPrice").toString());
		printVo.setTotalPaid(order.getFinalTotalFee().toString());
		List<Good> goods = new ArrayList<>();
		for (OrderItem orderItem : orderItems) {
			Good good = new Good();
			good.setCount(orderItem.getDishNum());
			good.setPrice(orderItem.getPrice().toString());
			good.setGoodsName(orderItem.getName());
		}
		printVo.setGoods(goods);
		String post = HttpKit.post(PropKit.get("orderPrintAddress"), JsonKit.toJson(printVo));
		@SuppressWarnings("unchecked")
		Map<String, String> parseObject = JSON.parseObject(post, Map.class);
		if ("200".equals(parseObject.get("code"))){
			order.set("isPrint", "Y").update();
		}
	}
	
	
	public void print1() {
		Order order = orderService.getOrderById(getPara("id"));
		OrderItem oi = new OrderItem().setOrderId(order.getId());
		List<OrderItem> orderItems = orderItemService.getAllByEqualAttr(oi, "orderId");
		PrintVo printVo = new PrintVo();
		printVo.setCreateTime(order.getCreateTime());
		printVo.setTotalOrder(order.getBigDecimal("totalPrice").toString());
		printVo.setTotalPaid(order.getFinalTotalFee().toString());
		List<Good> goods = new ArrayList<>();
		for (OrderItem orderItem : orderItems) {
			Good good = new Good();
			good.setCount(orderItem.getDishNum());
			good.setPrice(orderItem.getPrice().toString());
			good.setGoodsName(orderItem.getName());
		}
		printVo.setGoods(goods);
		String post = HttpKit.post(PropKit.get("orderPrintAddress"), JsonKit.toJson(printVo));
		@SuppressWarnings("unchecked")
		Map<String, String> parseObject = JSON.parseObject(post, Map.class);
		if ("200".equals(parseObject.get("code"))){
			order.set("isPrint", "Y").update();
		}
	}
	
}
