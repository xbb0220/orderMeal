package cn.orderMeal.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import cn.orderMeal.common.kit.SqlKit;
import cn.orderMeal.common.kit.SqlKit.PageInfo;
import cn.orderMeal.common.model.Order;
import cn.orderMeal.common.service.OrderService;

public class OrderServiceImpl extends BaseServiceImpl<Order> implements OrderService {
	public static final OrderServiceImpl service = new OrderServiceImpl();

	@Override
	public Order getOrderById(String id) {
		String sql = Db.getSql("order.getOrderById");
		Order order = super.findFirst(sql, id);
		return order;
	}

	@Override
	public Page<Order> page(PageInfo pageInfo, Order order) {
		StringBuffer sql = new StringBuffer(Db.getSql("order.page"));
		List<Object> params = new ArrayList<>();
		SqlKit.appendEqualCondition(false, sql, order, params, "guestId");
		sql.append(" group by order_item.orderId ");
		sql.append(" order by order_item.createTime ");
		return super.paginate(pageInfo.getPage(), pageInfo.getPageSize(), sql.toString(), params.toArray());
	}
	
}
