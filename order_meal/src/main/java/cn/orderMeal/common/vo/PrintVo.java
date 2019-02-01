package cn.orderMeal.common.vo;

import java.util.Date;
import java.util.List;

public class PrintVo {
	private Date createTime;
	private String totalOrder;
	private String totalPaid;
	private List<Good> goods;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTotalOrder() {
		return totalOrder;
	}

	public void setTotalOrder(String totalOrder) {
		this.totalOrder = totalOrder;
	}

	public String getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(String totalPaid) {
		this.totalPaid = totalPaid;
	}

	public List<Good> getGoods() {
		return goods;
	}

	public void setGoods(List<Good> goods) {
		this.goods = goods;
	}

}
