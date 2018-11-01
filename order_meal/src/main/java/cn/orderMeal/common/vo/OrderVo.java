package cn.orderMeal.common.vo;

import java.util.List;

import cn.orderMeal.common.model.OrderItem;

public class OrderVo {
	private String diningTableId;
	private String diningTabeNum;
	private List<OrderItem> orderItems;

	public String getDiningTableId() {
		return diningTableId;
	}

	public void setDiningTableId(String diningTableId) {
		this.diningTableId = diningTableId;
	}

	public String getDiningTabeNum() {
		return diningTabeNum;
	}

	public void setDiningTabeNum(String diningTabeNum) {
		this.diningTabeNum = diningTabeNum;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

}
