package cn.orderMeal.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseOrderItem<M extends BaseOrderItem<M>> extends Model<M> implements IBean {

	public M setId(java.lang.String id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.String getId() {
		return getStr("id");
	}

	public M setDishId(java.lang.String dishId) {
		set("dishId", dishId);
		return (M)this;
	}
	
	public java.lang.String getDishId() {
		return getStr("dishId");
	}

	public M setPrice(java.math.BigDecimal price) {
		set("price", price);
		return (M)this;
	}
	
	public java.math.BigDecimal getPrice() {
		return get("price");
	}

	public M setDeleteFlag(java.lang.String deleteFlag) {
		set("deleteFlag", deleteFlag);
		return (M)this;
	}
	
	public java.lang.String getDeleteFlag() {
		return getStr("deleteFlag");
	}

	public M setCreateTime(java.util.Date createTime) {
		set("createTime", createTime);
		return (M)this;
	}
	
	public java.util.Date getCreateTime() {
		return get("createTime");
	}

	public M setUpdateTime(java.util.Date updateTime) {
		set("updateTime", updateTime);
		return (M)this;
	}
	
	public java.util.Date getUpdateTime() {
		return get("updateTime");
	}

	public M setDishNum(java.lang.Integer dishNum) {
		set("dishNum", dishNum);
		return (M)this;
	}
	
	public java.lang.Integer getDishNum() {
		return getInt("dishNum");
	}

	public M setOrderId(java.lang.String orderId) {
		set("orderId", orderId);
		return (M)this;
	}
	
	public java.lang.String getOrderId() {
		return getStr("orderId");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}
	
	public java.lang.String getName() {
		return getStr("name");
	}

	public M setPicUrl(java.lang.String picUrl) {
		set("picUrl", picUrl);
		return (M)this;
	}
	
	public java.lang.String getPicUrl() {
		return getStr("picUrl");
	}

}
