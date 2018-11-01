package cn.orderMeal.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseDict<M extends BaseDict<M>> extends Model<M> implements IBean {

	public M setId(java.lang.String id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.String getId() {
		return getStr("id");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}
	
	public java.lang.String getName() {
		return getStr("name");
	}

	public M setCode(java.lang.String code) {
		set("code", code);
		return (M)this;
	}
	
	public java.lang.String getCode() {
		return getStr("code");
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

	public M setDictTypeId(java.lang.String dictTypeId) {
		set("dictTypeId", dictTypeId);
		return (M)this;
	}
	
	public java.lang.String getDictTypeId() {
		return getStr("dictTypeId");
	}

}
