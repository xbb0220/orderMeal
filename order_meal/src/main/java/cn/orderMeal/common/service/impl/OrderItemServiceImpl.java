package cn.orderMeal.common.service.impl;

import java.util.List;

import cn.orderMeal.common.model.OrderItem;
import cn.orderMeal.common.service.OrderItemService;

public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem> implements OrderItemService {
	public static final OrderItemServiceImpl service = new OrderItemServiceImpl();

	@Override
	public List<OrderItem> getOrderItemByOrderId(String orderId) {
		String sql = super.getSql("orderItem.getOrderItemByOrderId");
		return super.find(sql, orderId);
	}
}
