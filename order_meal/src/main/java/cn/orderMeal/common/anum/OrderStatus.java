package cn.orderMeal.common.anum;

public enum OrderStatus {
	WAITING_PAY("waitingPay", "待付款"), CANCEL("calcel", "完成"), FINISH("finish", "完成");
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
