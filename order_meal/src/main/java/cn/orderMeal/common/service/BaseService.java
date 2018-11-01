package cn.orderMeal.common.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

public interface BaseService<T extends Model<T>> {

    public String getSql(String sqlKey);

    public boolean save(T entity);

    public boolean delete(T entity);

    public boolean update(T entity);

    public boolean deleteById(Object id);

    public boolean deleteById(Object... ids);

    public List<T> find(SqlPara sqlPara);

    public List<T> find(String sql);

    public List<T> find(String sql, Object... paras);

    public T findById(Object idValue);

    public T findById(Object... idValues);

    public T findByIdLoadColumns(Object idValue, String columns);

    public T findByIdLoadColumns(Object[] idValues, String columns);

    public T findFirst(SqlPara sqlPara);

    public T findFirst(String sql);

    public T findFirst(String sql, Object... paras);

    public Page<T> paginate(int pageNumber, int pageSize, String sql, Object... paras);

    public Page<T> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect);

    public Page<T> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras);

    public Page<T> paginate(int pageNumber, int pageSize, boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras);
    
    public Page<T> paginateDynamicEqual(int pageNumber, int pageSize, T model, String... attrs);

    public List<T> getAll();

    public List<T> getAllByEqualAttr(T entity, String... columns);

    public List<T> getAllByEqualAttr(boolean isDynamicQuery, T entity, String... columns);

    public List<T> getAllByEqualAttr(boolean isDynamicQuery, String loadColumns, T entity, String... columns);

    public T getByEqualAttr(T entity, String... columns);

    public T getByEqualAttr(boolean isDynamicQuery, T entity, String... columns);

    public T getByEqualAttr(boolean isDynamicQuery, String loadColumns, T entity, String... columns);

    public long getCountByEqualAttr(T entity, String... columns);

    public long getCountByEqualAttr(boolean isDynamicQuery, T entity, String... columns);

}
