package com.zml.oa.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deepoove.poi.XWPFTemplate;
import com.zml.oa.entity.Project;
import com.zml.oa.entity.WorkOrder;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IBaseService;
import com.zml.oa.service.IWorkOrderService;
import com.zml.oa.util.DateUtil;

@Service
public class WorkOrderServiceImpl implements IWorkOrderService {
	@Autowired 
	private IBaseService<WorkOrder> baseService;
	
	@Autowired 
	private IBaseService<Project> projectService;
	
	@Override
	public Serializable doAdd(WorkOrder workOrder) throws Exception {
		return this.baseService.add(workOrder);
	}

	@Override
	public void doUpdate(WorkOrder workOrder,boolean flag) throws Exception {
		this.baseService.update(workOrder);
		if(flag) {
			projectService.update(workOrder.getProject());
		}
	}

	@Override
	public void doDelete(WorkOrder workOrder) throws Exception {
		this.baseService.delete(workOrder);
	}

	@Override
	public List<WorkOrder> toList(Integer userId) throws Exception {
		return this.baseService.findByPage("WorkOrder", new String[] {"applyUserId"}, new String[] {userId.toString()});
	}

	@Override
	public WorkOrder findById(Integer id) throws Exception {
		return this.baseService.getUnique("WorkOrder", new String[]{"id"}, new String[]{id.toString()});
	}

	@Override
	public List<WorkOrder> findByStatus(Integer userId, String status, Page<WorkOrder> page) throws Exception {
		List<WorkOrder> list = this.baseService.getListPage("WorkOrder", new String[]{"applyUserId","status"}, new String[]{userId.toString(), status}, page);
		return list;
	}

	@Override
	public List<WorkOrder> getWorkOrderList(Page<WorkOrder> page) throws Exception {		
		return this.baseService.getListPage("WorkOrder", new String[]{}, new String[]{}, page);		
	}

	@Override
	public List<WorkOrder> getWorkOrderList(Page<WorkOrder> page,String[] columns, String[] values, String sort, String order) throws Exception {
		return this.baseService.getListPage("WorkOrder", columns, values, page, sort, order);
	}
	@Override
	public List<WorkOrder> getWorkOrderList(String hql,Page<WorkOrder> page,String sort,String order,Object[] values)throws Exception{
		return this.baseService.getListPage(hql, page, sort, order, values);
	}
	@Override
	public List<WorkOrder> getWorkOrderList(String hql,String sort,String order,Object[] values)throws Exception{
		return this.baseService.getList(hql, sort, order, values);
	}
	@Override
	public XWPFTemplate generatePrintWorkorder(String servletContextRealPath, WorkOrder workOrder) {
		String templatePath;
		if(workOrder.getPriority()!=null&&workOrder.getPriority()==60) {
			templatePath = "/template/template_60.docx";
		}else {
		    templatePath = "/template/template_50.docx";
		}
        // 获取模板文件
        File templateFile = new File(servletContextRealPath + templatePath);
        // 替换读取到的 word 模板内容的指定字段
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PROJECT_NAME",workOrder.getDomain().getName());
        params.put("NO",workOrder.getId().toString());
        params.put("DOMAIN",workOrder.getDomain().getDomain());
        params.put("DEVELOP_EXPLAIN",workOrder.getDevelopExplain());
        params.put("APPLY_USER",workOrder.getApplyUser());
        params.put("APPLY_DATE",DateUtil.DateToString(workOrder.getApplyDate(),"yyyy-MM-dd"));
        params.put("B_AUDIT",workOrder.getBusinessAuditUser());
        params.put("B_DATE",DateUtil.DateToString(workOrder.getBusinessAuditDate(),"yyyy-MM-dd"));
        params.put("CODER_VERSION",workOrder.getCoderVersion());
        params.put("CODER_SVN",workOrder.getCoderSvn());
        params.put("CODER",workOrder.getCoder());
        params.put("CODER_DATE",DateUtil.DateToString(workOrder.getCoderDate(),"yyyy-MM-dd"));
        params.put("C_AUDIT",workOrder.getCoderAudit());
        params.put("C_DATE",DateUtil.DateToString(workOrder.getCoderAuditDate(),"yyyy-MM-dd"));
        params.put("TEST_SVN",workOrder.getTestSvn());
        params.put("TEST_VERSION",workOrder.getTestVersion());
        params.put("TESTER",workOrder.getTester());
        params.put("TESTER_DATE",DateUtil.DateToString(workOrder.getTesterDate(),"yyyy-MM-dd"));
        params.put("T_AUDIT",workOrder.getTesterAudit());
        params.put("T_DATE",DateUtil.DateToString(workOrder.getTestAuditDate(),"yyyy-MM-dd"));
        params.put("WEB",workOrder.getWebMaster());
        params.put("WEB_DATE",DateUtil.DateToString(workOrder.getDeployDate(),"yyyy-MM-dd"));
        params.put("W_AUDIT",workOrder.getWebMasterAudit());
        params.put("W_DATE",DateUtil.DateToString(workOrder.getWebMasterAuditDate(),"yyyy-MM-dd"));
        params.put("VERIFY_DATE",DateUtil.DateToString(workOrder.getVerifyDate(),"yyyy-MM-dd"));
        params.put("HOME", workOrder.getDomain().getSubjection());
        params.put("TYPE", workOrder.getType());
        params.put("AREA", workOrder.getDomain().getArea());
        params.put("COMMITER", workOrder.getCommiter());
        params.put("COMMITER_DATE", DateUtil.DateToString(workOrder.getCommiterDate(),"yyyy-MM-dd"));
        params.put("TEST_DESC", workOrder.getTestDesc());
        if("1".equals(workOrder.getRollbackFlag())) {
        	params.put("ROLLBACK", "回滚");
        }
        XWPFTemplate template = XWPFTemplate.compile(templateFile)
				.render(params);
		return template;
	}
}
