package cn.orderMeal.common.service;

import com.jfinal.plugin.activerecord.Page;

import cn.orderMeal.common.kit.SqlKit.PageInfo;
import cn.orderMeal.common.model.Order;

public interface OrderService extends BaseService<Order> {
	
	public Order getOrderById(String id);
	
	public Page<Order> page(PageInfo pageInfo, Order order);
}
