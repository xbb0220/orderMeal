package cn.orderMeal.common.controller;

import java.util.List;

import cn.orderMeal.common.model.OrderItem;
import cn.orderMeal.common.service.DinningTableService;
import cn.orderMeal.common.service.DishService;
import cn.orderMeal.common.service.OrderItemService;
import cn.orderMeal.common.service.OrderService;
import cn.orderMeal.common.service.impl.DinningTableServiceImpl;
import cn.orderMeal.common.service.impl.DishServiceImpl;
import cn.orderMeal.common.service.impl.OrderItemServiceImpl;
import cn.orderMeal.common.service.impl.OrderServiceImpl;

public class OrderItemController extends BaseController{

	DishService dishService = DishServiceImpl.service;
	DinningTableService dinningTableService = DinningTableServiceImpl.service;
	OrderService orderService = OrderServiceImpl.service;
	OrderItemService orderItemService = OrderItemServiceImpl.service;
	
	public void list() {
		OrderItem queryModel = simpleModel(OrderItem.class);
		List<OrderItem> orderItems = orderItemService.getAllByEqualAttr(false, queryModel, "orderId");
		renderJson(orderItems);
	}
	
}
