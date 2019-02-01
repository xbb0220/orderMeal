package cn.orderMeal.common.task;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;

import cn.orderMeal.common.anum.OrderStatus;
import cn.orderMeal.common.model.Order;
import cn.orderMeal.common.service.OrderService;
import cn.orderMeal.common.service.impl.OrderServiceImpl;

public class OrderStatusTask implements Runnable{
	OrderService orderService = OrderServiceImpl.service;
	
	@Override
	public void run() {
		String orderForCancelSql = Db.getSql("order.orderForCancel");
		List<Order> orderForCances = orderService.find(orderForCancelSql, OrderStatus.WAITING_PAY.getCode());
		for (Order orderForCance : orderForCances) {
			orderForCance.setStatus(OrderStatus.CANCEL.getCode());
			orderService.update(orderForCance);
		}
	}

}
