package com.zml.oa.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.zml.oa.dao.IBaseDao;

@Repository
public class BaseDaoImpl<T> implements IBaseDao<T> {
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Session getSession() {
	    //事务必须是开启的(Required)，否则获取不到
	    return sessionFactory.getCurrentSession();
	}
	
	@Override
	public Serializable add(T bean) throws Exception{
		return getSession().save(bean) ;
	}
	
	@Override
	public void saveOrUpdate(T bean) throws Exception{
		this.getSession().saveOrUpdate(bean);
	}
	
	@Override
	public void delete(T bean) throws Exception{
		this.getSession().delete(bean);
	}
	
	@Override
	public void update(T bean) throws Exception{
		this.getSession().update(bean);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> createQuery(String queryString) throws Exception{
		List<T> list = this.getSession().createQuery(queryString).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getBean(Class<T> obj, Serializable id) throws Exception{
		return (T) getSession().get(obj, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T loadBean(Class<T> obj, Serializable id) throws Exception{
		return (T) getSession().load(obj, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByPage(String hql, int firstResult, int maxResult) throws Exception{
		Session session=sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResult);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T unique(String hql) throws Exception{
		Query query = getSession().createQuery(hql);
	    return (T) query.uniqueResult();
	}

	@Override
	public Long count(String hql) {
		return (Long) this.getSession().createQuery(hql).uniqueResult();
	}
	
	@Override
	public Integer count(String hql,Object ... params) {
		if(StringUtils.isNotBlank(hql)) {			
			Query query = this.getSession().createQuery(hql);
			if(params!=null) {
				for (int i = 0; i < params.length; i++) {
					Object object = params[i];
					query.setParameter(i, object);
				}
			}
			List list = query.list();
			if(list!=null)
				return list.size();
		}
		return 0;
	}

	@Override
	public List<T> findByPage(String hql, int firstResult, int maxResult, String sort, String order) throws Exception {
		Session session=sessionFactory.getCurrentSession();
		if(StringUtils.isNotBlank(sort)&&StringUtils.isNotBlank(order)) {
			hql = hql+" order by "+sort+" "+order;
		}
		Query query = session.createQuery(hql);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResult);
		return query.list();
	}
	@Override
	public List<T> findByPage(String hql, int firstResult, int maxResult, String sort, String order,Object ... params) throws Exception {
		Session session=sessionFactory.getCurrentSession();
		if(StringUtils.isNotBlank(sort)&&StringUtils.isNotBlank(order)) {
			hql = hql+" order by "+sort+" "+order;
		}
		Query query = session.createQuery(hql); 
		if(params!=null) {
			for (int i = 0; i < params.length; i++) {
				Object object = params[i];
				query.setParameter(i, object);
			}
		}		
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResult);
		return query.list();
	}
	@Override
	public List<T> find(String hql, String sort, String order,Object ... params) throws Exception{
		Session session=sessionFactory.getCurrentSession();
		if(StringUtils.isNotBlank(sort)&&StringUtils.isNotBlank(order)) {
			hql = hql+" order by "+sort+" "+order;
		}
		Query query = session.createQuery(hql); 
		if(params!=null) {
			for (int i = 0; i < params.length; i++) {
				Object object = params[i];
				query.setParameter(i, object);
			}
		}
		return query.list();
	}
}
