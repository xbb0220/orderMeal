package cn.orderMeal.common.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.Redis;

import cn.orderMeal.common.anum.OrderStatus;
import cn.orderMeal.common.cons.SessionConst;
import cn.orderMeal.common.kit.AjaxJson;
import cn.orderMeal.common.kit.GenerateOrderNoKit;
import cn.orderMeal.common.kit.SqlKit.PageInfo;
import cn.orderMeal.common.model.DinningTable;
import cn.orderMeal.common.model.Dish;
import cn.orderMeal.common.model.Guest;
import cn.orderMeal.common.model.Order;
import cn.orderMeal.common.model.OrderItem;
import cn.orderMeal.common.service.DinningTableService;
import cn.orderMeal.common.service.DishService;
import cn.orderMeal.common.service.OrderItemService;
import cn.orderMeal.common.service.OrderService;
import cn.orderMeal.common.service.impl.DinningTableServiceImpl;
import cn.orderMeal.common.service.impl.DishServiceImpl;
import cn.orderMeal.common.service.impl.OrderItemServiceImpl;
import cn.orderMeal.common.service.impl.OrderServiceImpl;
import cn.orderMeal.common.vo.OrderVo;

public class OrderController extends BaseController{

	DishService dishService = DishServiceImpl.service;
	DinningTableService dinningTableService = DinningTableServiceImpl.service;
	OrderService orderService = OrderServiceImpl.service;
	OrderItemService orderItemService = OrderItemServiceImpl.service;
	
	/**
	 * 设置桌号信息到订单中
	 * @param orderVo   前端传过来的订单信息
	 * @param order		用于下单的订单对象
	 */
	private void setDinningTableInfo(OrderVo orderVo, Order order) {
		DinningTable dinningTable = dinningTableService.findById(orderVo.getDiningTableId());
		if (null != dinningTable){
			order.setDiningTableId(orderVo.getDiningTableId());
			order.setDiningTableNum(dinningTable.getDiningTableNum());
		} else{
			order.setDiningTableNum(orderVo.getDiningTableNum());
		}
	}
	
	/**
	 * 
		{
			"diningTabeNum": "100",  桌号
			"diningTableId": "1",    桌子id
			"numberOfDinner": 1,		 就餐人数
			"dinnerTime": "2018-01-01 15:15:15",  就餐时间
			"remark": "sdfds"		 订单备注
			"orderItems": [{			 菜品项
				"dishId": "1",		 菜主键
				"dishNum": 5			 菜数量
			}, {
				"dishId": "1",
				"dishNum": 6
			}]
		} 
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public void order() throws UnsupportedEncodingException, IOException{
		String orderStr = readPostStr();
		OrderVo orderVo = JSON.parseObject(orderStr, OrderVo.class);
		
		Order order = new Order();
		order.setRemark(orderVo.getRemark());//下单备注
		order.setPhoneNo(orderVo.getPhoneNo());//联系人电话号码
		order.setDinnerTime(orderVo.getDinnerTime());//就餐时间
		order.setNumberOfDinner(orderVo.getNumberOfDinner());//就餐人数
		setDinningTableInfo(orderVo, order);
		Guest guest = getSessionAttr(SessionConst.GUEST_INFO);
		order.setGuestId(guest.getId()).setStatus(OrderStatus.WAITING_PAY.getCode());//订单和当前登录用户关联，并设置订单状态为待付款
		order.setOutTradeNo(GenerateOrderNoKit.gen("R", 530L));//生成订单号
		
		Calendar orderTime = Calendar.getInstance();
		order.setTimeStart(orderTime.getTime());
		orderTime.add(Calendar.MINUTE, 30);
		order.setTimeExpire(orderTime.getTime());//订单失效时间，30分钟后未付款变成取消订单
		
		order.setOpenId(guest.getOpenid());//订单付款人员openId，高用付款接口时要用
		orderService.save(order);
		List<OrderItem> orderItems = orderVo.getOrderItems();//获取客户点菜列表
		for (OrderItem orderItem : orderItems){
			Dish dish = dishService.findById(orderItem.getDishId());
			orderItem.setPrice(dish.getPrice());
			orderItem.setPicUrl(dish.getPicUrl());
			orderItem.setName(dish.getName());
			orderItem.setOrderId(order.getId());//关联到订单
			orderItemService.save(orderItem);
		}
		Order orderById = orderService.getOrderById(order.getId());
		orderById.setTotalFee(orderById.getBigDecimal("totalPrice").multiply(new BigDecimal(100)).intValue());
		orderService.update(orderById);
		renderJson(AjaxJson.success().setData(order.getId()));
	}
	
	public void getOrderById(){
		Order order = orderService.getOrderById(getPara("id"));
		if (null == order){
			renderJson(AjaxJson.failure().setMsg("找不到该订单信息"));
			return;
		} else{
			OrderItem oi = new OrderItem().setOrderId(order.getId());
			List<OrderItem> orderItems = orderItemService.getAllByEqualAttr(oi, "orderId");
			order.put("orderItems", orderItems);
			renderJson(AjaxJson.success().setData(order));
		}
	}
	
	private static final String orderItemCacheKey = "order_item_cache";
	
	public void page(){
		PageInfo pageInfo = getBean(PageInfo.class, "");
		Guest guest = getSessionAttr(SessionConst.GUEST_INFO);
		Order order = simpleModel(Order.class);
		order.setGuestId(guest.getId());
		Page<Order> page = orderService.page(pageInfo, order); 
		for(Order or : page.getList()) {
			List<OrderItem> orderItems = Redis.use().get(orderItemCacheKey + or.getId());
			if (null == orderItems) {
				orderItems = orderItemService.getOrderItemByOrderId(or.getId());
				Redis.use().setex(orderItemCacheKey + or.getId(), 200, orderItems);
			}
			or.put("orderItems", orderItems);
		}
		renderJson(page);
	}
	
	public void orderStatus() {
		List<Record> res = new ArrayList<>();
		for (OrderStatus orderStatu : OrderStatus.values()) {
			Record record = new Record().set("code", orderStatu.getCode()).set("des", orderStatu.getDes());
			res.add(record);
		}
		renderJson(res);
	}
}
