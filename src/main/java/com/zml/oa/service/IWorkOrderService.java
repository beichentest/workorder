package com.zml.oa.service;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.deepoove.poi.XWPFTemplate;
import com.zml.oa.entity.WorkOrder;
import com.zml.oa.pagination.Page;
import com.zml.oa.util.DateUtil;

public interface IWorkOrderService {
	public Serializable doAdd(WorkOrder workOrder) throws Exception;
	
	public void doUpdate(WorkOrder workOrder,boolean flag) throws Exception;
	
	public void doDelete(WorkOrder workOrder) throws Exception;
	
	public List<WorkOrder> toList(Integer userId) throws Exception;
	
	public WorkOrder findById(Integer id) throws Exception;
	
	public List<WorkOrder> findByStatus(Integer userId, String status, Page<WorkOrder> page) throws Exception; 
	
	public List<WorkOrder> getWorkOrderList(Page<WorkOrder> page) throws Exception;
	
	public List<WorkOrder> getWorkOrderList(Page<WorkOrder> page,String[] columns, String[] values,String sort,String order) throws Exception;
	
	public List<WorkOrder> getWorkOrderList(String hql,Page<WorkOrder> page,String sort,String order,Object[] values)throws Exception;
	
	public List<WorkOrder> getWorkOrderList(String hql,String sort,String order,Object[] values)throws Exception;
	
	public XWPFTemplate generatePrintWorkorder(String servletContextRealPath, WorkOrder workOrder)throws Exception;
}
