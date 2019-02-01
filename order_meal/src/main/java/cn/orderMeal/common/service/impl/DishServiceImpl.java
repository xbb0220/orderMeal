package cn.orderMeal.common.service.impl;

import cn.orderMeal.common.model.Dish;
import cn.orderMeal.common.service.DishService;

public class DishServiceImpl extends BaseServiceImpl<Dish> implements DishService {
	public static final DishServiceImpl service = new DishServiceImpl();
}
