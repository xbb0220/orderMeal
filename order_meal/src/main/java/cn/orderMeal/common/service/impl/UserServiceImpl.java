package cn.orderMeal.common.service.impl;

import cn.orderMeal.common.model.User;
import cn.orderMeal.common.service.UserService;

public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
	public static final UserServiceImpl service = new UserServiceImpl();
}
