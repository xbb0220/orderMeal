package cn.orderMeal.common.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Page;

import cn.orderMeal.common.cons.SessionConst;
import cn.orderMeal.common.kit.AjaxJson;
import cn.orderMeal.common.kit.SqlKit.PageInfo;
import cn.orderMeal.common.kit.session.SessionKit;
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
		
	public void show(){
		List<OrderItem> orderItems = new ArrayList<>();
		new OrderItem().setDishId("1").setDishNum(6);
		orderItems.add(new OrderItem().setDishId("1").setDishNum(5));
		orderItems.add(new OrderItem().setDishId("1").setDishNum(6));
		
		OrderVo ov = new OrderVo();
		ov.setDiningTableId("1");
		ov.setDiningTabeNum("100");
		ov.setOrderItems(orderItems);
		renderJson(ov);
	}
	
	/**
	 * 
		{
			"diningTabeNum": "100",
			"diningTableId": "1",
			"orderItems": [{
				"dishId": "1",
				"dishNum": 5
			}, {
				"dishId": "1",
				"dishNum": 6
			}]
		} 
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public void order() throws UnsupportedEncodingException, IOException{
		String orderStr = getPara("json");
		OrderVo orderVo = JSON.parseObject(orderStr, OrderVo.class);
		Order order = new Order();
		DinningTable dinningTable = dinningTableService.findById(orderVo.getDiningTableId());
		if (null != dinningTable){
			order.setDiningTableId(orderVo.getDiningTableId());
			order.setDiningTabeNum(dinningTable.getDiningTabeNum());
		} else{
			order.setDiningTabeNum(orderVo.getDiningTabeNum());
		}
		Guest guest = SessionKit.getAttr(SessionConst.GUEST_INFO);
		order.setGuestId(guest.getId()).setStatus("order");
		orderService.save(order);
		List<OrderItem> orderItems = orderVo.getOrderItems();
		for (OrderItem orderItem : orderItems){
			Dish dish = dishService.findById(orderItem.getDishId());
			orderItem.setPrice(dish.getPrice());
			orderItem.setPicUrl(dish.getPicUrl());
			orderItem.setName(dish.getName());
			orderItem.setOrderId(order.getId());
			orderItemService.save(orderItem);
		}
		renderJson(AjaxJson.success());
	}
	
	public void getOrderById(){
		Order order = orderService.getOrderById(getPara("id"));
		if (null == order){
			renderJson(AjaxJson.failure().setMsg("找不到该订单"));
			return;
		} else{
			renderJson(AjaxJson.success().setData(order));
		}
	}
	
	public void page(){
		PageInfo pageInfo = getBean(PageInfo.class, "");
		Guest guest = SessionKit.getAttr(SessionConst.GUEST_INFO);
		Order order = simpleModel(Order.class);
		order.setGuestId(guest.getId());
		Page<Order> page = orderService.page(pageInfo, order);
		renderJson(page);
	}
	
}
