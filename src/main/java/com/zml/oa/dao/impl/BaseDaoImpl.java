package com.zml.oa.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.engine.query.spi.HQLQueryPlan;
import org.hibernate.engine.query.spi.QueryPlanCache;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.zml.oa.dao.IBaseDao;
import com.zml.oa.util.DAOUtil;

@Repository
public class BaseDaoImpl<T> extends HibernateDaoSupport implements IBaseDao<T> {
	private static final Logger logger = Logger.getLogger(BaseDaoImpl.class);

	/**
	 * 说明: 1.在既使用注解又使用HibernateDaoSupport的情况下,只能这么写,
	 * 原因是HibernateDaoSupport是抽象类,且方法都是final修饰的,
	 * 这样就不能为其注入sessionFactory或者hibernateTemplate
	 * 2.若使用xml配置的话,就可以直接给HibernateDaoSupport注入.
	 */
	// 而使用HibernateDaosupport,又必须为其注入sessionFactory或者hibernateTemplate

	// 这里为其注入sessionFactory,最后只需要让自己的Dao继承这个MyDaoSupport.
	// 不直接在自己的Dao里继承HibernateDaoSupport的原因是这样可以简化代码,
	// 不用每次都为其注入sessionFactory或者hibernateTemplate了,在这里注入一次就够了.
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	// 或者为其注入hibernateTemplate
	// @Resource(name="hibernateTemplate")
	// public void setSuperHibernateTemplate(HibernateTemplate hibernateTemplate){
	// super.setHibernateTemplate(hibernateTemplate);
	// }

	@Override
	public Serializable add(T bean) throws Exception {
		return getHibernateTemplate().save(bean);
		// return getSession().save(bean) ;
	}

	@Override
	public void saveOrUpdate(T bean) throws Exception {
		getHibernateTemplate().merge(bean);
		// this.getSession().saveOrUpdate(bean);
	}

	@Override
	public void delete(T bean) throws Exception {
		getHibernateTemplate().delete(bean);
		// this.getSession().delete(bean);
	}

	@Override
	public void update(T bean) throws Exception {
		getHibernateTemplate().update(bean);
		// this.getSession().update(bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> createQuery(String queryString) throws Exception {
		return (List<T>) getHibernateTemplate().find(queryString);
		// List<T> list = this.getSession().createQuery(queryString).list();
		// return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getBean(Class<T> obj, Serializable id) throws Exception {
		return getHibernateTemplate().get(obj, id);
		// return (T) getSession().get(obj, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T loadBean(Class<T> obj, Serializable id) throws Exception {
		return getHibernateTemplate().load(obj, id);
		// return (T) getSession().load(obj, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByPage(String hql, int firstResult, int maxResult) throws Exception {
		return getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				query.setFirstResult(firstResult);
				query.setMaxResults(maxResult);
				return query.list();
			}
		});
		// Session session=sessionFactory.getCurrentSession();
		// Query query = session.createQuery(hql);
		// query.setFirstResult(firstResult);
		// query.setMaxResults(maxResult);
		// return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T unique(String hql) throws Exception {
		return getHibernateTemplate().execute(new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				return (T) query.uniqueResult();
			}
		});
		// Query query = getSession().createQuery(hql);
		// return (T) query.uniqueResult();
	}

	@Override
	public Long count(String hql) {
		return getHibernateTemplate().execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				return (Long) query.uniqueResult();
			}
		});
		// return (Long) this.getSession().createQuery(hql).uniqueResult();
	}

	@Override
	public Integer count(String hql, Object... params) {
		if (StringUtils.isNotBlank(hql)) {
			return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
				@Override
				public Integer doInHibernate(Session session) throws HibernateException {
					String tempHql = hql.toLowerCase();
					tempHql = hql.substring(hql.indexOf("from"), hql.length());
					if (tempHql.indexOf("order by") > 0)
						tempHql = tempHql.substring(0, tempHql.indexOf("order by"));
					int whereIndex = tempHql.indexOf("where");
					int leftIndex = tempHql.indexOf("left");
					if (leftIndex >= 0) {
						if (whereIndex >= 0) {
							String temp = StringUtils.substringBefore(tempHql, "left");
							tempHql = temp + " where " + StringUtils.substringAfter(tempHql, "where");
						} else {
							tempHql = StringUtils.substringBefore(tempHql, "left");
						}
					}
					tempHql = "select count(*) " + tempHql;
					Query query = session.createQuery(tempHql);
					if (params != null) {
						for (int i = 0; i < params.length; i++) {
							Object object = params[i];
							query.setParameter(i, object);
						}
					}
					int totalCount = ((Long) query.iterate().next()).intValue();
					return totalCount;
				}
			});
		}
		// if(StringUtils.isNotBlank(hql)) {
		// Query query = this.getSession().createQuery(hql);
		// if(params!=null) {
		// for (int i = 0; i < params.length; i++) {
		// Object object = params[i];
		// query.setParameter(i, object);
		// }
		// }
		// List list = query.list();
		// if(list!=null)
		// return list.size();
		// }
		return 0;
	}

	@Override
	public List<T> findByPage(String hql, int firstResult, int maxResult, String sort, String order) throws Exception {
		return getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException {
				String hqlDo = hql;
				if (StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(order)) {
					hqlDo = hql + " order by " + sort + " " + order;
				}
				Query query = session.createQuery(hqlDo);
				query.setFirstResult(firstResult);
				query.setMaxResults(maxResult);
				return query.list();
			}
		});
		// Session session=sessionFactory.getCurrentSession();
		// if(StringUtils.isNotBlank(sort)&&StringUtils.isNotBlank(order)) {
		// hql = hql+" order by "+sort+" "+order;
		// }
		// Query query = session.createQuery(hql);
		// query.setFirstResult(firstResult);
		// query.setMaxResults(maxResult);
		// return query.list();
	}

	@Override
	public List<T> findByPage(String hql, int firstResult, int maxResult, String sort, String order, Object... params)
			throws Exception {
		return getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException {
				String hqlDo = hql;
				if (StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(order)) {
					hqlDo = hql + " order by " + sort + " " + order;
				}
				Query query = session.createQuery(hqlDo);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						Object object = params[i];
						query.setParameter(i, object);
					}
				}
				query.setFirstResult(firstResult);
				query.setMaxResults(maxResult);
				return query.list();
			}
		});
		// Session session=sessionFactory.getCurrentSession();
		// if(StringUtils.isNotBlank(sort)&&StringUtils.isNotBlank(order)) {
		// hql = hql+" order by "+sort+" "+order;
		// }
		// Query query = session.createQuery(hql);
		// if(params!=null) {
		// for (int i = 0; i < params.length; i++) {
		// Object object = params[i];
		// query.setParameter(i, object);
		// }
		// }
		// query.setFirstResult(firstResult);
		// query.setMaxResults(maxResult);
		// return query.list();
	}

	@Override
	public List<T> find(String hql, String sort, String order, Object... params) throws Exception {
		return getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException {
				String hqlDo = hql;
				if (StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(order)) {
					hqlDo = hql + " order by " + sort + " " + order;
				}
				Query query = session.createQuery(hqlDo);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						Object object = params[i];
						query.setParameter(i, object);
					}
				}
				return query.list();
			}
		});
		// Session session=sessionFactory.getCurrentSession();
		// if(StringUtils.isNotBlank(sort)&&StringUtils.isNotBlank(order)) {
		// hql = hql+" order by "+sort+" "+order;
		// }
		// Query query = session.createQuery(hql);
		// if(params!=null) {
		// for (int i = 0; i < params.length; i++) {
		// Object object = params[i];
		// query.setParameter(i, object);
		// }
		// }
		// return query.list();
	}

	@Override
	public List<T> findAllByIObjectCType(Class iClass) {
		return (List<T>) getHibernateTemplate().find("from " + iClass.getName());
	}

	@Override
	public List<T> findByIObjectCType(Class iClass, int page, int pageSize) {
		return findPageByCriteria(DetachedCriteria.forClass(iClass), pageSize, page);
	}

	@Override
	public int execByHQL(String hSQL) throws Exception {
		return getHibernateTemplate().bulkUpdate(hSQL);
	}

	@Override
	public int execByHQL(String hSQL, T[] t) {
		return getHibernateTemplate().bulkUpdate(hSQL, t);
	}

	@Override
	public List<T> findAllByCriteria(DetachedCriteria detachedCriteria) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				return criteria.list();
			}
		});
	}

	@Override
	public List<T> findPageByCriteria(DetachedCriteria detachedCriteria, int pageSize, int page) {
		return findPageByCriteria(detachedCriteria, pageSize, (page > 0) ? (page - 1) * pageSize : page * pageSize,
				true, page);
	}

	@Override
	public int getCountByCriteria(DetachedCriteria detachedCriteria) {
		Integer count = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				return criteria.setProjection(Projections.rowCount()).uniqueResult();
			}
		});
		return count;
	}

	@Override
	public List getAllBySQL(String sql, String[] fields) {
		List list = new ArrayList();
		Session session = getSessionFactory().openSession();
		session.doWork(new Work() {
			@Override
			public void execute(java.sql.Connection conn) {
				java.sql.ResultSet rs = null;
				try {
					Map form = null;
					rs = conn.createStatement().executeQuery(sql);
					while (rs.next()) {
						form = new HashMap();
						for (int i = 0; i < fields.length; i++) {
							form.put(fields[i], rs.getObject(fields[i]) == null ? "" : rs.getObject(fields[i]));
						}
						list.add(form);
					}
				} catch (SQLException ex) {
					logger.error("======", ex);
				} finally {
					try {
						if (rs != null) {
							rs.close();
						}
					} catch (SQLException ex1) {
						logger.error("======", ex1);
					}
				}

			}
		});
		session.flush();
		return list;
	}

	@Override
	public List<T> findPageBySQL(Class clazz, String sql, List params, int pageSize, int page) {
		return findPageBySQL(clazz, sql, params, pageSize, (page > 0) ? (page - 1) * pageSize : page * pageSize, page);
	}

	@Override
	public Integer findPageBySQLCount(String sql, Object[] params) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				String tempHql = sql.toLowerCase();

				tempHql = tempHql.substring(tempHql.indexOf(" from "), sql.length());
				if (tempHql.indexOf("order by") > 0)
					tempHql = tempHql.substring(0, tempHql.indexOf("order by"));
				tempHql = "select count(*) " + tempHql;

				Query query = session.createSQLQuery(tempHql);
				if (params != null && params.length > 0) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(i, params[i]);
					}
				}
				int totalCount = Integer.parseInt(query.list().iterator().next().toString());
				return totalCount;
			}
		});
	}

	@Override
	public Integer findPageBySQLSimpleCount(String sql) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				String tempHql = sql.toLowerCase();
				int totalCount = Integer.parseInt(session.createSQLQuery(tempHql).list().iterator().next().toString());
				return totalCount;
			}
		});
	}

	@Override
	public List<T> findBySQL(String sql) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {

				Query query = session.createSQLQuery(sql);
				List list = query.list();

				return list;
			}
		});
	}

	@Override
	public List<T> findBySQL(Class clazz, String sql, List params) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (params != null && params.size() > 0) {
					for (int i = 0; i < params.size(); i++) {
						query.setParameter(i, params.get(i));
					}
				}
				List list = null;
				try {
					list = DAOUtil.queryMapsToList(clazz, query.list());
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					logger.error("======", e);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					logger.error("======", e);
				}
				return list;
			}
		});
	}

	@Override
	public int execBySQL(String sql) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(sql);
				return query.executeUpdate();
			}
		});
	}

	/**
	 * 分页查询单表
	 * 
	 * @param detachedCriteria
	 * @param pageSize
	 * @param startIndex
	 * @param isPage
	 * @param page
	 * @return
	 */
	private List<T> findPageByCriteria(final DetachedCriteria detachedCriteria, final int pageSize,
			final int startIndex, boolean isPage, final int page) {
		return (List<T>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				int totalCount = (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
				criteria.setProjection(null);
				return criteria.setFirstResult(startIndex).setMaxResults(pageSize).list();
			}
		});
	}

	private List<T> findPageBySQL(final Class clazz, final String sql, final List params, final int pageSize,
			final int startIndex, final int page) {
		return (List<T>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				String tempHql = null;
				String sqls = sql.toLowerCase();
				tempHql = "select count(*) from (" + sqls + " ) temp_a";
				Query queryCount = session.createSQLQuery(tempHql);
				Query query = session.createSQLQuery(sqls).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (params != null && params.size() > 0) {
					for (int i = 0; i < params.size(); i++) {
						queryCount.setParameter(i, params.get(i));
						query.setParameter(i, params.get(i));
					}
				}
				int totalCount = Integer.parseInt(queryCount.list().iterator().next().toString());
				query.setFirstResult(startIndex);
				query.setMaxResults(pageSize);
				List list = null;
				try {
					list = DAOUtil.queryMapsToList(clazz, query.list());
				} catch (InstantiationException e) {
					logger.error("======", e);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					logger.error("======", e);
				}
				return list;
			}
		});
	}

	protected String prepareCountHql(String hql) {
		String fromHql = hql;
		fromHql = "from" + StringUtils.substringAfter(fromHql, "from");
		fromHql = StringUtils.substringBefore(fromHql, "order by");
		int whereIndex = fromHql.indexOf("where");
		int leftIndex = fromHql.indexOf("left join");
		if (leftIndex >= 0) {
			if (whereIndex >= 0) {
				String temp = StringUtils.substringBefore(fromHql, "left");
				fromHql = temp + " where " + StringUtils.substringAfter(fromHql, "where");
			} else {
				fromHql = StringUtils.substringBefore(fromHql, "left");
			}
		}
		String countHql = "select count(*) " + fromHql;
		return countHql;
	}

	protected String getCountSql(String originalHql, SessionFactory sessionFactory) {
		SessionFactoryImplementor sessionFactoryImplementor = (SessionFactoryImplementor) sessionFactory;
		HQLQueryPlan hqlQueryPlan = sessionFactoryImplementor.getQueryPlanCache().getHQLQueryPlan(originalHql, false,
				Collections.emptyMap());
		String[] sqls = hqlQueryPlan.getSqlStrings();
		String countSql = "select count(*) from (" + sqls[0] + ") count";
		return countSql;
	}

	public SessionFactoryImplementor getSessionFactoryImplementor() {
		return (SessionFactoryImplementor) getSessionFactory();
	}

	public QueryPlanCache getQueryPlanCache() {
		return getSessionFactoryImplementor().getQueryPlanCache();
	}

	public HQLQueryPlan getHqlQueryPlan(String hql) {
		return getQueryPlanCache().getHQLQueryPlan(hql, false, Collections.emptyMap());
	}

	protected String prepareCountSql(String sql) {
		return getCountSql(sql, getSessionFactory());
	}
}
