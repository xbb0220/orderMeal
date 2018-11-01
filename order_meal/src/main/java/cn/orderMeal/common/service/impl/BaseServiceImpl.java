package cn.orderMeal.common.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.PageSqlKit;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.TableMapping;

import cn.orderMeal.common.kit.GenericsKit;
import cn.orderMeal.common.kit.SqlKit;
import cn.orderMeal.common.service.BaseService;




public class BaseServiceImpl<T extends Model<T>> implements BaseService<T>{

	@SuppressWarnings("unchecked")
	Class<T> clazz = GenericsKit.getSuperClassGenricType(getClass());
	Model<T> model = newInstance();
	
	private Model<T> newInstance(){
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getSql(String sqlKey) {
		return model.getSql(sqlKey);
	}
	
	@Override
	public boolean save(T entity) {
		String [] primaryKeys = TableMapping.me().getTable(clazz).getPrimaryKey();
		if (primaryKeys.length == 1) {
			entity.set("id", UUID.randomUUID().toString());
		}
		Set<String> columns = TableMapping.me().getTable(clazz).getColumnTypeMap().keySet();
		if (columns.contains("createTime") && null == entity.get("createTime")) {
			entity.set("createTime", new Date());
		}
		return entity.save();
	}
	
	
	@Override
	public boolean update(T entity){
		return entity.update();
	}
	
	@Override
	public boolean delete(T entity) {
		return entity.delete();
	}

	@Override
	public boolean deleteById(Object id) {
		return model.deleteById(id);
	}

	@Override
	public boolean deleteById(Object... ids) {
		return model.deleteById(ids);
	}

	@Override
	public List<T> find(SqlPara sqlPara) {
		return model.find(sqlPara);
	}

	@Override
	public List<T> find(String sql) {
		return model.find(sql);
	}

	@Override
	public List<T> find(String sql, Object... paras) {
		return model.find(sql, paras);
	}

	@Override
	public T findById(Object idValue) {
		return model.findById(idValue);
	}

	@Override
	public T findById(Object... idValues) {
		return model.findById(idValues);
	}

	@Override
	public T findByIdLoadColumns(Object[] idValues, String columns) {
		return model.findByIdLoadColumns(idValues, columns);
	}

	@Override
	public T findByIdLoadColumns(Object idValue, String columns) {
		return model.findByIdLoadColumns(idValue, columns);
	}

	@Override
	public T findFirst(SqlPara sqlPara) {
		return model.findFirst(sqlPara);
	}

	@Override
	public T findFirst(String sql) {
		return model.findFirst(sql);
	}

	@Override
	public T findFirst(String sql, Object... paras) {
		return model.findFirst(sql, paras);
	}

	@Override
	public Page<T> paginate(int pageNumber, int pageSize, String sql, Object... paras) {
		String[] sqls = PageSqlKit.parsePageSql(sql);
		return model.paginate(pageNumber, pageSize, sqls[0], sqls[1], paras);
	}
	
	/**
	 * 
	* @Title: paginate 
	*@author vic
	* @Description: TODO
	* @param @param pageNumber
	* @param @param pageSize
	* @param @param select
	* @param @param sqlExceptSelect from
	* @param @return     
	* @return 
	* @throws
	 */
	@Override
	public Page<T> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect) {
		return model.paginate(pageNumber, pageSize, select, sqlExceptSelect);
	}

	@Override
	public Page<T> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
		return model.paginate(pageNumber, pageSize, select, sqlExceptSelect, paras);
	}

	@Override
	public Page<T> paginate(int pageNumber, int pageSize, boolean isGroupBySql, String select, String sqlExceptSelect,
			Object... paras) {
		return model.paginate(pageNumber, pageSize, isGroupBySql, select, sqlExceptSelect, paras);
	}

	@Override
	public List<T> getAll() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ").append(TableMapping.me().getTable(clazz).getName());
		return model.find(sql.toString());
	}

	@Override
	public List<T> getAllByEqualAttr(T entity, String... columns) {
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sbSql.append(" select * from " + TableMapping.me().getTable(clazz).getName());
		sbSql.append(" where 1=1 ");
		SqlKit.appendEqualCondition(sbSql, entity, params, columns);
		return model.find(sbSql.toString(), params.toArray());
	}

	@Override
	public List<T> getAllByEqualAttr(boolean isDynamicQuery, T entity, String... columns) {
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sbSql.append(" select * from " + TableMapping.me().getTable(clazz).getName());
		sbSql.append(" where 1=1 ");
		SqlKit.appendEqualCondition(isDynamicQuery, sbSql, entity, params, columns);
		return model.find(sbSql.toString(), params.toArray());
	}

	@Override
	public List<T> getAllByEqualAttr(boolean isDynamicQuery, String loadColumns, T entity, String... columns) {
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sbSql.append(" select ").append(loadColumns).append(" from " + TableMapping.me().getTable(clazz).getName());
		sbSql.append(" where 1=1 ");
		SqlKit.appendEqualCondition(isDynamicQuery, sbSql, entity, params, columns);
		return model.find(sbSql.toString(), params.toArray());
	}

	@Override
	public T getByEqualAttr(T entity, String... columns) {
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sbSql.append(" select * from " + TableMapping.me().getTable(clazz).getName());
		sbSql.append(" where 1=1 ");
		SqlKit.appendEqualCondition(sbSql, entity, params, columns);
		return model.findFirst(sbSql.toString(), params.toArray());
	}

	@Override
	public T getByEqualAttr(boolean isDynamicQuery, T entity, String... columns) {
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sbSql.append(" select * from " + TableMapping.me().getTable(clazz).getName());
		sbSql.append(" where 1=1 ");
		SqlKit.appendEqualCondition(isDynamicQuery, sbSql, entity, params, columns);
		return model.findFirst(sbSql.toString(), params.toArray());
	}

	@Override
	public T getByEqualAttr(boolean isDynamicQuery, String loadColumns, T entity, String... columns) {
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sbSql.append(" select ").append(loadColumns).append(" from " + TableMapping.me().getTable(clazz).getName());
		sbSql.append(" where 1=1 ");
		SqlKit.appendEqualCondition(isDynamicQuery, sbSql, entity, params, columns);
		return model.findFirst(sbSql.toString(), params.toArray());
	}

	@Override
	public long getCountByEqualAttr(T entity, String... columns) {
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sbSql.append(" select count(*) total from " + TableMapping.me().getTable(clazz).getName());
		sbSql.append(" where 1=1 ");
		SqlKit.appendEqualCondition(sbSql, entity, params, columns);
		return Db.queryFirst(sbSql.toString(),  params.toArray());
	}

	@Override
	public long getCountByEqualAttr(boolean isDynamicQuery, T entity, String... columns) {
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sbSql.append(" select count(*) total from " + TableMapping.me().getTable(clazz).getName());
		sbSql.append(" where 1=1 ");
		SqlKit.appendEqualCondition(isDynamicQuery, sbSql, entity, params, columns);
		return Db.queryFirst(sbSql.toString(),  params.toArray());
	}

	@Override
	public Page<T> paginateDynamicEqual(int pageNumber, int pageSize, T model, String... attrs) {
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sbSql.append(" select * from " + TableMapping.me().getTable(clazz).getName());
		sbSql.append(" where 1=1 ");
		SqlKit.appendEqualCondition(sbSql, model, params, attrs);
		return paginate(pageNumber, pageSize, sbSql.toString(), params.toArray());
	}

}
