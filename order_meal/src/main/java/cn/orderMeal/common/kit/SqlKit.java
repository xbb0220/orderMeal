package cn.orderMeal.common.kit;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.jfinal.plugin.activerecord.Model;

public class SqlKit {

	public static class PageInfo {
		private int page;
		private int pageSize;
		private String sort;
		private String order;

		public int getPage() {
			return page;
		}

		public void setPage(int page) {
			this.page = page;
		}

		public void setRows(int rows) {
			this.pageSize = rows;
		}
		
		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public String getSort() {
			return sort;
		}

		public void setSort(String sort) {
			this.sort = sort;
		}

		public String getOrder() {
			return order;
		}

		public void setOrder(String order) {
			this.order = order;
		}
		
		public static PageInfo OneResult(){
			PageInfo pageInfo = new PageInfo();
			pageInfo.setPageSize(1);
			pageInfo.setPage(1);
			return pageInfo;
		}

	}

	/**
	 * easyui表格排序信息附加
	 * 如没有其它排序信息，则用在最末尾，否则用在其它排序函数之前
	 * @param pageInfo
	 * @param sqlExceptSelect
	 */
	public static void appendOrderInfo(PageInfo pageInfo, StringBuffer sqlExceptSelect){
		if (StringKit.isNotEmpty(pageInfo.getSort())){
			sqlExceptSelect.append(" order by ").append(pageInfo.getSort());
		}
		if (StringKit.isNotEmpty(pageInfo.getOrder())){
			sqlExceptSelect.append( " " + pageInfo.getOrder() + " ");
		}
	}
	
	
	public static void appendDefaultAscOrderInfo(StringBuffer sqlExceptSelect,String sort){
		if(sqlExceptSelect.indexOf("order by") == -1){
			sqlExceptSelect.append(" order by ").append(sort).append(" asc ");
		}
	}
	
	public static void appendDefaultDescOrderInfo(StringBuffer sqlExceptSelect,String sort){
		if(sqlExceptSelect.indexOf("order by") == -1){
			sqlExceptSelect.append(" order by ").append(sort).append(" desc ");
		}
	}
	
	/**
	 * 附加等值条件查询
	 * @param sqlExceptSelect
	 * @param model
	 * @param columns
	 */
	public static void appendEqualCondition(StringBuffer sqlExceptSelect, Model<?> model,List<Object> params, String... columns){
		for (String column : columns){
			Object paramValue = column.indexOf(".") == -1 
					? model.get(column) : model.get(column.substring(column.indexOf(".") + 1));
			if (paramValue != null){
				if (paramValue.getClass().equals(String.class)){
					if (StringKit.isNotEmpty(String.valueOf(paramValue))){
						sqlExceptSelect.append(" and ").append(column).append(" =? ");
						params.add(paramValue);
					}
				}
				else if (paramValue.getClass().equals(Date.class) || paramValue.getClass().equals(Timestamp.class) || paramValue.getClass().equals(java.sql.Date.class)){
					sqlExceptSelect.append(" and date_format(" + column + ", '%Y-%m-%d') = ? ");
					params.add(DateKit.formatDate((Date) paramValue, DateKit.DEFAULT_FORMAT));
				}
				else if(paramValue.getClass().equals(Integer.class)){
					sqlExceptSelect.append(" and ").append(column).append(" =? ");
					params.add(paramValue);
				}
			}
		}
	}
	
	public static void appendEqualCondition(boolean isDynamicQuery, StringBuffer sqlExceptSelect, Model<?> model,List<Object> params, String... columns){
		if (isDynamicQuery){
			appendEqualCondition(sqlExceptSelect, model, params, columns);
		}
		else{
			appendNotDynamicEqualCondition(sqlExceptSelect, model, params, columns);
		}
	}
	
	public static void appendNotDynamicEqualCondition(StringBuffer sqlExceptSelect, Model<?> model,List<Object> params, String... columns){
		for (String column : columns) {
			Object paramValue = column.indexOf(".") == -1 ? model.get(column)
					: model.get(column.substring(column.indexOf(".") + 1));
			if (null == paramValue){
				sqlExceptSelect.append(" and ").append(column).append(" =? ");
				params.add(paramValue);
				continue;
			}
			if (paramValue.getClass().equals(String.class)) {
				if (StringKit.isNotEmpty(String.valueOf(paramValue))) {
					sqlExceptSelect.append(" and ").append(column).append(" =? ");
					params.add(paramValue);
				}
			} else if (paramValue.getClass().equals(Date.class) || paramValue.getClass().equals(Timestamp.class)
					|| paramValue.getClass().equals(java.sql.Date.class)) {
				sqlExceptSelect.append(" and date_format(" + column + ", '%Y-%m-%d') = ? ");
				params.add(DateKit.formatDate((Date) paramValue, DateKit.DEFAULT_FORMAT));
			} else if (paramValue.getClass().equals(Integer.class)) {
				sqlExceptSelect.append(" and ").append(column).append(" =? ");
				params.add(paramValue);
			}
		}
	}
	
	/**
	 * 附加like条件查询
	 * @param sqlExceptSelect
	 * @param model
	 * @param params
	 * @param columns
	 */
	public static void appendLikeCondition(StringBuffer sqlExceptSelect, Model<?> model,List<Object> params, String... columns){
		for (String column : columns){
			Object paramValue = column.indexOf(".") == -1 
					? model.get(column) : model.get(column.substring(column.indexOf(".") + 1));
			if (paramValue != null){
				if (paramValue.getClass().equals(String.class)){
					if (StringKit.isNotEmpty(String.valueOf(paramValue))){
						sqlExceptSelect.append(" and ").append(column).append(" like ? ");
						params.add("%" +paramValue + "%");
					}
				}
			}
		}
	}
	
	public static void appendDescOrderInfo(StringBuffer sqlExceptSelect, String... columns){
		if (columns.length == 0){
			throw new RuntimeException("columns 参数不能空！");
		}
		if (-1 == sqlExceptSelect.indexOf("order")){
			sqlExceptSelect.append(" order by " + columns[0] + " desc ");
		}
		else{
			sqlExceptSelect.append(" , ").append(columns[0]).append(" desc ");
		}
		for (int i = 1; i < columns.length; i++) {
			sqlExceptSelect.append(" , ").append(columns[i]).append(" desc ");
		}
	}
	
	public static void appendInCondition(StringBuffer sqlExceptSelect,List<Object> params,String columnName, String... values){
		if (null == values || values.length == 0){
			return;
		}
		else{
			sqlExceptSelect.append(" and ").append(columnName).append("  in ( ");
			for (int i = 0; i < values.length - 1; i ++){
				sqlExceptSelect.append("?, ");
				params.add(values[i]);
			}
			sqlExceptSelect.append("? )");params.add(values[values.length - 1]);
		}
	}
	
	public static void appendAscOrderInfo(StringBuffer sqlExceptSelect, String... columns){
		if (columns.length == 0){
			throw new RuntimeException("columns 参数不能空！");
		}
		if (-1 == sqlExceptSelect.indexOf("order")){
			sqlExceptSelect.append(" order by " + columns[0] + " asc ");
		}
		else{
			sqlExceptSelect.append(" , ").append(columns[0]).append(" asc ");
		}
		for (int i = 1; i < columns.length; i++) {
			sqlExceptSelect.append(" , ").append(columns[i]).append(" asc ");
		}
	}
	
	public static PageInfo getEasyUiReqestInfo(HttpServletRequest request){
		PageInfo pageInfo = new PageInfo();
		pageInfo.setSort(request.getParameter("sort"));
		pageInfo.setOrder(request.getParameter("order"));
		int page = StringKit.isInteger(request.getParameter("page")) ? Integer.valueOf(request.getParameter("page")) : 1;
		int rows = StringKit.isInteger(request.getParameter("rows")) ? Integer.valueOf(request.getParameter("rows")) : 10;
		rows = rows > 1000 ? 1000 : rows;
		pageInfo.setPage(page);
		pageInfo.setPageSize(rows);
		return pageInfo;
	}
	
}
