/**
 * Project Name:SpringOA
 * File Name:IBaseService.java
 * Package Name:com.zml.oa.service
 * Date:2014-11-9下午5:37:16
 *
 */
package com.zml.oa.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.zml.oa.pagination.Page;

/**
 * @ClassName: IBaseService
 * @Description:IBaseService
 * @author: zml
 * @date: 2014-11-9 下午5:37:16
 *
 */
public interface IBaseService<T> {

	 public List<T> getAllList(String tableSimpleName) throws Exception;
	 
	 public T getUnique(String tableSimpleName,String[] columns,String[] values) throws Exception;
	 
	 public List<T> findByWhere(String tableSimpleName,String[] columns,String[] values) throws Exception;
	 
	 public List<T> getCount(String tableSimpleName) throws Exception;
	 
	 public Serializable add(T bean) throws Exception;
	 
	 public void saveOrUpdate(T bean) throws Exception;

	 public void delete(T bean) throws Exception;
	 
	 public void update(T bean) throws Exception;
	 
	 public T getBean(final Class<T> obj,final Serializable id) throws Exception;
	 
	 public List<T> findByPage(String tableSimpleName,String[] columns,String[] values) throws Exception;
	 
	 public List<T> getListPage(String tableSimpleName,String[] columns,String[] values, Page<T> page) throws Exception;
	 
	 public List<T> getListPage(String tableSimpleName,String[] columns,String[] values, Page<T> page,String sort,String order) throws Exception;
	 
	 public List<T> getListPage(String hql,  Page<T> page, String sort,String order , Object ... params) throws Exception;
	
	 public List<T> getList(String hql,  String sort,String order , Object ... params) throws Exception;
	 
	 public T loadBean(final Class<T> obj,final Serializable id) throws Exception;
	 
	 /**
		 * 传入的数据对象类型查找数据
		 * 
		 * @param iClass
		 *            Class
		 * @return List
		 */
		public List<T> findAllByIObjectCType(final Class iClass);
		/**
		 * 传入的数据对象类型查找数据（分页）
		 * @param iClass
		 * @param page
		 * @param pageSize
		 * @return
		 */
		public List<T> findByIObjectCType(final Class iClass, final int page,
				final int pageSize);
		
		/**
		 * 批量执行hql
		 * 
		 * @param iNameQuery
		 *            String
		 * @return List
		 */
		public int execByHQL(final String hSQL) throws Exception;

		/**
		 * 批量执行hql
		 * 
		 * @param iNameQuery
		 *            String
		 * @return List
		 */
		public int execByHQL(final String hSQL, T[] t);
		
		/**
		 * 
		 * detachedCriteria 查询
		 * 
		 * @param detachedCriteria
		 *            DetachedCriteria
		 * @return List
		 */
		public List<T> findAllByCriteria(final DetachedCriteria detachedCriteria);
		/**
		 * detachedCriteria 分页查询
		 * @param detachedCriteria
		 * @param pageSize
		 * @param page
		 * @return
		 */
		public List<T> findPageByCriteria(final DetachedCriteria detachedCriteria, final int pageSize,final int page);
		/**
		 * detachedCriteria 查询所有记录数
		 * 
		 * @param detachedCriteria
		 *            DetachedCriteria
		 * @return int
		 */
		public int getCountByCriteria(final DetachedCriteria detachedCriteria);
		
		/**
		 * 使用sql查询
		 * 
		 * @param sql
		 *            要执行的sql
		 * @param fields
		 *            要查询的field
		 */
		public List getAllBySQL(String sql, String[] fields);
		/**
		 * 带分页的sql语句查询(传参,并转换为自定义对象)
		 * @param clazz 自定义对象
		 * @param sql
		 * @param params
		 * @param pageSize
		 * @param page
		 * @return
		 */
		public List<T> findPageBySQL(final Class clazz,final String sql,final List params,final int pageSize, final int page);
		/**
		 * 获得sql查询结果的记录数
		 * @param sql
		 * @return
		 */
		public Integer findPageBySQLCount(final String sql,final Object[] params);
		
		/**
		 * 简单的通过sql查询记录总数
		 * @param sql
		 * @return
		 */
		public Integer findPageBySQLSimpleCount(final String sql);
		/**
		 * 根据本地sql语言查询
		 * @param sql
		 * @return
		 */
		public List<T> findBySQL(final String sql);
		/**
		 * 根据本地sql语言查询
		 * @param clazz 欲转换自定义类型
		 * @param sql  
		 * @param params 参数值
		 * @return
		 */
		public List<T> findBySQL(final Class clazz,final String sql,final List params);
		/**
		 * 执行本地sql操作
		 * @param sql
		 * @return
		 */
		public int execBySQL(final String sql);
}
