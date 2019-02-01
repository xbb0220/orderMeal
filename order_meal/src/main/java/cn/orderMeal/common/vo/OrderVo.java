package cn.orderMeal.common.vo;

import java.util.Date;
import java.util.List;

import cn.orderMeal.common.model.OrderItem;

public class OrderVo {
	private String diningTableId;
	private String diningTableNum;
	private Integer numberOfDinner;
	private String phoneNo;
	private Date dinnerTime;
	private List<OrderItem> orderItems;
	private String remark;
	
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getNumberOfDinner() {
		return numberOfDinner;
	}

	public void setNumberOfDinner(Integer numberOfDinner) {
		this.numberOfDinner = numberOfDinner;
	}

	public Date getDinnerTime() {
		return dinnerTime;
	}

	public void setDinnerTime(Date dinnerTime) {
		this.dinnerTime = dinnerTime;
	}

	public String getDiningTableId() {
		return diningTableId;
	}

	public void setDiningTableId(String diningTableId) {
		this.diningTableId = diningTableId;
	}
	
	public String getDiningTableNum() {
		return diningTableNum;
	}

	public void setDiningTableNum(String diningTableNum) {
		this.diningTableNum = diningTableNum;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

}
