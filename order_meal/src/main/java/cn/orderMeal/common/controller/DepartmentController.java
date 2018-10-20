package cn.orderMeal.common.controller;

import com.jfinal.core.Controller;

import cn.orderMeal.common.model.Department;

public class DepartmentController extends Controller{

	public void index(){
		Department department = getModel(Department.class, "");
		renderJson(department);
	}
	
}
