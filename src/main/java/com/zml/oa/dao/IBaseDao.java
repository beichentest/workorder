package com.zml.oa.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

public interface IBaseDao<T> {
	/**
	 * 
	 * @Title: save
	 * @Description: 保存实体
	 * @param: @param bean
	 * @param: @return   
	 * @return: Serializable   
	 * @throws
	 */
	public Serializable add(T bean) throws Exception;

	/**
	 * 
	 * @Title: saveOrUpdate
	 * @Description: 保存或者更新实体
	 * @param: @param bean   
	 * @return: void   
	 * @throws
	 */
	public void saveOrUpdate(T bean) throws Exception;


	/**
	 * 
	 * @Title: delete
	 * @Description: 删除
	 * @param: @param clazz
	 * @param: @param id   
	 * @return: void   
	 * @throws
	 */
	public void delete(T bean) throws Exception;

	/**
	 * 
	 * @Title: update
	 * @Description: 更新实体
	 * @param: @param bean   
	 * @return: void   
	 * @throws
	 */
	public void update(T bean) throws Exception;
	
	/**
	 * 
	 * @Title: update
	 * @Description: 执行HQL
	 * @param: @param queryString   
	 * @return: List<T>   
	 * @throws
	 */
	public List<T> createQuery(final String hql) throws Exception;
	
	/**
	 * 
	 * @Title: getBean
	 * @Description: 根据ID获取实体
	 * @param: @param obj
	 * @param: @param id
	 * @param: @return   
	 * @return: T   
	 * @throws
	 */
	public T getBean(final Class<T> obj,final Serializable id) throws Exception;

	/**
	 * 
	 * @Title: findByPage
	 * @Description: 分页-无条件
	 * @param: @param hql
	 * @param: @param values
	 * @param: @return   
	 * @return: List<T>   
	 * @throws
	 */
	public List<T> findByPage(final String hql, int firstResult, int maxResult) throws Exception;
	/**
	 * 分页查询Hql
	 * @param hql
	 * @param firstResult
	 * @param maxResult
	 * @param sort
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public List<T> findByPage(final String hql, int firstResult, int maxResult,String sort,String order) throws Exception;
	/**
	 * 分页Hql查询带参数
	 * @param hql
	 * @param firstResult
	 * @param maxResult
	 * @param sort
	 * @param order
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<T> findByPage(String hql, int firstResult, int maxResult, String sort, String order,Object ... params) throws Exception;
	/**
	 * 非分页查询
	 * @param hql
	 * @param sort
	 * @param order
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<T> find(String hql, String sort, String order,Object ... params) throws Exception;
	/**
	 * 
	 * @Title: unique
	 * @Description: 返回唯一一条数据
	 * @param: @return   
	 * @return: T   
	 * @throws
	 */
	public T unique(final String hql) throws Exception;
	
	/**
	 * Hql查询数量  
	 * @param hql = select count(*) from XXX
	 * @return
	 */
	public Long count(String hql);
	/**
	 * 计算Hql语句返回结果数量，允许？号传参
	 * @param hql
	 * @param params 
	 * @return
	 */
	public Integer count(String hql,Object ... params);
	/**
	 * 懒加载查询对象
	 * @param obj
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public T loadBean(Class<T> obj, Serializable id) throws Exception;
	
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
