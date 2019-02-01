package cn.orderMeal.common.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.jfinal.core.Controller;
import com.jfinal.core.converter.TypeConverter;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

public abstract class BaseController extends Controller{

	public String readPostStr() throws UnsupportedEncodingException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(getRequest().getInputStream(),"utf-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T simpleModel(Class<T> modelClass){
		TypeConverter converter = TypeConverter.me();
		Model<?> model = (Model<?>) createInstance(modelClass);
		Table table = TableMapping.me().getTable((Class<? extends Model<?>>) modelClass);
		if (null == table){
			throw new IllegalArgumentException("mode类和数据表不存在映射关系");
		}
		Map<String, Class<?>> columnTypeMap = table.getColumnTypeMap();
		Set<String> columnNameSet = getParaMap().keySet();
		for (Entry<String, Class<?>> columnTypeEntry : columnTypeMap.entrySet()) {
			if (columnNameSet.contains(columnTypeEntry.getKey())) {
				String paraStr = getPara(columnTypeEntry.getKey());
				Object value = null;
				try {
					value = (null == paraStr) ? null : converter.convert(columnTypeEntry.getValue(), paraStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				model.set(columnTypeEntry.getKey(), value);
			}
		}
		return (T) model;
	}
	
	private Object createInstance(Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
