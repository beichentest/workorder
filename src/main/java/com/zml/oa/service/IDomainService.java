package com.zml.oa.service;

import java.util.List;

import com.zml.oa.entity.Domain;
import com.zml.oa.pagination.Page;

public interface IDomainService {		
	public Domain getDomainById(Integer id)throws Exception;

	public List<Domain> getDomainList(Page<Domain> page,String[] columns, String[] values, String sort, String order) throws Exception;

	public List<Domain> getDomainList(String hql,Page<Domain> page,Object[] values, String sort, String order) throws Exception;
	
	public List<Domain> getDomainList(String hql,Object[] params, String sort, String order)throws Exception;
	
	public void doAdd(Domain project)throws Exception;
	
	public void doDelete(Integer id)throws Exception;
	
	public void doUpdate(Domain project)throws Exception;

	public void doDelete(Integer id, String status)throws Exception;

	public List<Domain> findByOnline()throws Exception ;
	
	public List<Domain> findDomainByProjectId(Integer projectId)throws Exception;
}
