package cn.orderMeal.common.service.impl;

import cn.orderMeal.common.model.OrderItem;
import cn.orderMeal.common.service.OrderItemService;

public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem> implements OrderItemService {
	public static final OrderItemServiceImpl service = new OrderItemServiceImpl();
}
