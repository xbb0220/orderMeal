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
		queryModel.setDeleteFlag("n");
		List<Dish> all = dishService.getAllByEqualAttr(false, queryModel, "dishTypeId", "deleteFlag");
		renderJson(all);
	}
	
	public void getAllDish(){
		DishType queryDishType = new DishType();
		queryDishType.setDeleteFlag("n");
		List<DishType> dishTypes = dishTypeService.getAllByEqualAttr(false, queryDishType, "deleteFlag");
		
		for (DishType dishType : dishTypes){
			Dish queryDishModel = new Dish();
			queryDishModel.setDishTypeId(dishType.getId());
			queryDishModel.setDeleteFlag("n");
			dishType.put("dishes", dishService.getAllByEqualAttr(false, queryDishModel, "dishTypeId", "deleteFlag"));
		}
		renderJson(dishTypes);
	}
	
}
