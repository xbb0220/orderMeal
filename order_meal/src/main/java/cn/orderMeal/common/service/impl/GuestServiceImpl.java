package cn.orderMeal.common.service.impl;

import cn.orderMeal.common.model.Guest;
import cn.orderMeal.common.service.GuestService;

public class GuestServiceImpl extends BaseServiceImpl<Guest> implements GuestService {
	public static final GuestServiceImpl service = new GuestServiceImpl();
}
