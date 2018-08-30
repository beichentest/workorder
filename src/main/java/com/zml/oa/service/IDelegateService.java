package com.zml.oa.service;

import java.util.Date;
import java.util.List;

import com.zml.oa.entity.Delegate;
import com.zml.oa.entity.Project;

public interface IDelegateService {
	public List<Delegate> findByAssignee(Integer assigneeId)throws Exception; 
	
	public Integer getAttorney(Integer assigneeId,Date time);
	
	public void doAdd(Delegate delegate)throws Exception;
	
	public void doDelete(Integer id)throws Exception;
	
	public void doUpdate(Project project)throws Exception;
}
