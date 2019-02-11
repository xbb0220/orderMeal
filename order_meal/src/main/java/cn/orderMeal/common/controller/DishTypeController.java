package cn.orderMeal.common.controller;

import java.util.List;
import cn.orderMeal.common.model.DishType;
import cn.orderMeal.common.service.DishTypeService;
import cn.orderMeal.common.service.impl.DishTypeServiceImpl;

public class DishTypeController extends BaseController{

	
	DishTypeService dishTypeService = DishTypeServiceImpl.service;
	
	
	public void getDishType(){
		List<DishType> all = dishTypeService.getAll();
		renderJson(all);
	}
	
	
}
