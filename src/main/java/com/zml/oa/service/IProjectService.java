package com.zml.oa.service;

import java.util.List;

import com.zml.oa.entity.Domain;
import com.zml.oa.entity.Project;
import com.zml.oa.pagination.Page;

public interface IProjectService {
	public List<Project> findByOnline()throws Exception; 
	
	public Project getProjectById(Integer id)throws Exception;

	public List<Project> getProjectList(Page<Project> page,String[] columns, String[] values, String sort, String order) throws Exception;

	public List<Project> getProjectList(String hql,Page<Project> page,Object[] values, String sort, String order) throws Exception;
	
	public List<Project> getProjectList(String hql,Object[] values, String sort, String order)throws Exception;
	
	public void doAdd(Project project)throws Exception;
	
	public void doDelete(Integer id)throws Exception;
	
	public void doUpdate(Project project)throws Exception;
}
