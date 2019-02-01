package cn.orderMeal.common.anum;

/**
 * 订单状态枚举
 * @author xubinbin
 */
public enum OrderStatus {
	WAITING_PAY("waitingPay", "待付款"), PAYING("paying", "付款中"), CANCEL("calcel", "取消"), FINISH("finish", "完成");
	String code;
	String des;

	private OrderStatus(String code, String des) {
		this.code = code;
		this.des = des;
	}

	public String getCode() {
		return code;
	}

	public String getDes() {
		return des;
	}

}
