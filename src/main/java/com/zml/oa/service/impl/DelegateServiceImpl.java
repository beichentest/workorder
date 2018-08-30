package com.zml.oa.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.zml.oa.entity.Delegate;
import com.zml.oa.entity.Project;
import com.zml.oa.service.IDelegateService;

@Service
public class DelegateServiceImpl extends BaseServiceImpl<Delegate> implements IDelegateService {

	@Override
	public List<Delegate> findByAssignee(Integer assigneeId) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Delegate.class);
		criteria.add(Restrictions.eq("assignee", assigneeId))
		        .add(Restrictions.and(Restrictions.eq("status", "0")));
		return this.findAllByCriteria(criteria);		
	}
	@Override
	public Integer getAttorney(Integer assigneeId,Date time) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Delegate.class);
		criteria.add(Restrictions.eq("assignee", assigneeId))
				.add(Restrictions.le("startTime", time))
				.add(Restrictions.ge("endTime", time))
		        .add(Restrictions.eq("status", "0"));
		List<Delegate> list = this.findAllByCriteria(criteria);
		if(list!=null&&list.size()>0) {
			return list.get(0).getAttorney();
		}
		return null;
	}
	
	@Override
	public void doAdd(Delegate delegate) throws Exception {
		this.add(delegate);
	}

	@Override
	public void doDelete(Integer id) throws Exception {
		String sql = "update t_delegate set status=1 where id="+id;
		this.execBySQL(sql);
	}

	@Override
	public void doUpdate(Project project) throws Exception {
		// TODO Auto-generated method stub

	}

}
