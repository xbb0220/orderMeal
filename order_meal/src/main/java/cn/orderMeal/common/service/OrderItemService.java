package cn.orderMeal.common.service;

import java.util.List;

import cn.orderMeal.common.model.OrderItem;

public interface OrderItemService extends BaseService<OrderItem> {
	
	List<OrderItem> getOrderItemByOrderId(String orderId);
	
}
