package cn.orderMeal.common.controller;

import java.util.List;

import cn.orderMeal.common.model.Dish;
import cn.orderMeal.common.model.DishType;
import cn.orderMeal.common.service.DishService;
import cn.orderMeal.common.service.DishTypeService;
import cn.orderMeal.common.service.impl.DishServiceImpl;
import cn.orderMeal.common.service.impl.DishTypeServiceImpl;

public class DishController extends BaseController{

	DishService dishService = DishServiceImpl.service;
	DishTypeService dishTypeService = DishTypeServiceImpl.service;
	
	public void getDish(){
		Dish queryModel = simpleModel(Dish.class);
		List<Dish> all = dishService.getAllByEqualAttr(false, queryModel, "dishTypeId");
		renderJson(all);
	}
	
	public void getAllDish(){
		List<DishType> dishTypes = dishTypeService.getAll();
		for (DishType dishType : dishTypes){
			Dish queryDishModel = new Dish();
			queryDishModel.setDishTypeId(dishType.getId());
			dishType.put("dishs", dishService.getAllByEqualAttr(false, queryDishModel, "dishTypeId"));
		}
		renderJson(dishTypes);
	}
	
}
