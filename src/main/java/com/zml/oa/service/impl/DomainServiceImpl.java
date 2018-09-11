package com.zml.oa.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zml.oa.entity.Domain;
import com.zml.oa.entity.Project;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IBaseService;
import com.zml.oa.service.IDomainService;

@Service
public class DomainServiceImpl extends BaseServiceImpl<Domain>implements IDomainService {
	@Autowired 
	private IBaseService<Domain> baseService;	
	@Override
	public Domain getDomainById(Integer id) throws Exception {
//		String sql = "select d from Domain d  left outer join fetch d.projects where d.id=? ";
//		Page<Domain> p = new Page<Domain>(0, 9999);		
//		String sort = "d.id";
//		String order = "asc";
//		List<Object> values = new ArrayList<Object>();
//		values.add(id);
//		List<Domain> list = getDomainList(sql, p, values.toArray(), sort,order);
//		if(list!=null&&list.size()>0)
//			return list.get(0);
		return baseService.getUnique("Domain", new String[] {"id"}, new String[] {id.toString()});
	}
	@Override
	public List<Domain> getDomainList(Page<Domain> page,String[] columns, String[] values, String sort, String order) throws Exception {		
		return getListPage("Domain", columns, values, page, sort, order);		
	}
	@Override
	public List<Domain> getDomainList(String hql,Page<Domain> page,Object[] values, String sort, String order) throws Exception {		
		return this.getListPage(hql, page, sort, order, values);		
	}
	@Override
	public List<Domain> getDomainList(String hql,Object[] params, String sort, String order)throws Exception{		
		return this.getList(hql, sort, order, params);
	}
	
	public void doAdd(Domain domain) throws Exception {
		add(domain);
	}
	@Override
	public void doDelete(Integer id) throws Exception {		
		Domain domain = baseService.getBean(Domain.class, id);
		domain.setDiscardDate(new Date());
		domain.setStatus("1");
		baseService.update(domain);
	}
	
	
	@Override
	public void doUpdate(Domain domain)throws Exception{
		baseService.update(domain);
	}
	@Override
	public void doDelete(Integer id, String status) throws Exception {
		Domain domain = baseService.getBean(Domain.class, id);
		if("1".equals(status)) {
			domain.setDiscardDate(new Date());
		}
		domain.setStatus(status);
		baseService.update(domain);
	}
	@Override
	public List<Domain> findByOnline() throws Exception {
		String sql = "select d from Domain d  left outer join fetch d.projects where d.status=0 ";
		Page<Domain> p = new Page<Domain>(0, 9999);		
		String sort = "d.id";
		String order = "asc";
		return getDomainList(sql, p, null, sort, order);		
	}
	
	@Override
	public List<Domain> findDomainByProjectId(Integer projectId)throws Exception{
		String sql = "select d.* from t_domain d,t_domain_project dp where d.id=dp.domain_id and dp.project_id=? and d.status=0 order by d.id";
		List<Integer> params = new ArrayList<Integer>();
		params.add(projectId);
		return this.findBySQL(Domain.class, sql, params);
	}
}
