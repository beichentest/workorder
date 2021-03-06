package com.zml.oa.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.deepoove.poi.XWPFTemplate;
import com.zml.oa.entity.Accessory;
import com.zml.oa.entity.BaseVO;
import com.zml.oa.entity.CommentVO;
import com.zml.oa.entity.Datagrid;
import com.zml.oa.entity.Domain;
import com.zml.oa.entity.Message;
import com.zml.oa.entity.Project;
import com.zml.oa.entity.User;
import com.zml.oa.entity.Vacation;
import com.zml.oa.entity.WorkOrder;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IAccessoryService;
import com.zml.oa.service.IDomainService;
import com.zml.oa.service.IProcessService;
import com.zml.oa.service.IProjectService;
import com.zml.oa.service.IVacationService;
import com.zml.oa.service.IWorkOrderService;
import com.zml.oa.util.BeanUtils;
import com.zml.oa.util.DateUtil;
import com.zml.oa.util.PoiExcelExport;
import com.zml.oa.util.UserUtil;

/**
 * @ClassName: WorkOrderAction
 * @Description:工单控制类,没有用动态任务分配
 * @author: chenli
 * @date: 2018-5-16 上午10:35:50
 *
 */

@Controller
@RequestMapping("/workOrderAction")
public class WorkOrderAction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2541344788225943147L;

	private static final Logger logger = Logger.getLogger(WorkOrderAction.class);

	@Autowired
	private IVacationService vacationService;

	@Autowired
	private IWorkOrderService workOrderService;

	@Autowired
	protected RuntimeService runtimeService;

	@Autowired
	protected IdentityService identityService;

	@Autowired
	protected TaskService taskService;

	@Autowired
	private IProcessService processService;

	@Autowired
	private FormService formService;

	@Autowired
	private IProjectService projectService;

	@Autowired
	private IDomainService domainService;
	
	@Autowired
	private IAccessoryService accessoryService;

	/**
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toList")
	@ResponseBody
	public Datagrid<WorkOrder> toList(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows, String sort, String order, String applyUser,
			String projectName, String type, String beginDate, String endDate) throws Exception {
		String hql = "select w from WorkOrder w where w.status='" + BaseVO.APPROVAL_SUCCESS + "'";
		Page<WorkOrder> p = new Page<WorkOrder>(page, rows);
		if (StringUtils.isBlank(sort)) {
			sort = "applyDate";
			order = "desc";
		}
		List<Object> values = new ArrayList<Object>();
		if (StringUtils.isNotBlank(projectName)) {
			hql += " and project.name like ? ";
			values.add("%" + projectName + "%");
		}
		if (StringUtils.isNotBlank(type)) {
			hql += " and type=? ";
			values.add(type);
		}
		if (StringUtils.isNotBlank(applyUser)) {
			hql += " and applyUser=? ";
			values.add(applyUser);
		}
		if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
			hql += " and testerDate >= ?  and testerDate<=? ";
			values.add(DateUtil.StringToDate(beginDate, "yyyy-MM-dd"));
			values.add(DateUtil.StringToDate(endDate, "yyyy-MM-dd"));
		}
		List<WorkOrder> workOrderList = this.workOrderService.getWorkOrderList(hql, p, sort, order, values.toArray());
		return new Datagrid<WorkOrder>(p.getTotal(), workOrderList);
	}

	@RequestMapping(value = "/workOrderDatagrid")
	public String toList_page(Model model) throws Exception {
		// List<Project> projectList = projectService.findByOnline();
		// model.addAttribute("projectList", projectList);
		return "workOrder/list_workOrder";
	}

	/**
	 * 以下是EasyUI的页面需求
	 * 
	 */

	/**
	 * 跳转添加页面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:workOrder:toAdd")
	@RequestMapping(value = "/toAdd")
	public String toAdd(Model model) throws Exception {
		List<Domain> domainList = domainService.findByOnline();
		model.addAttribute("domainList", domainList);
		return "workOrder/add_workOrder";
	}

	/**
	 * 添加并启动工单流程
	 *
	 * @param workOrder
	 */
	@RequiresPermissions("user:workOrder:doAdd")
	@RequestMapping(value = "/doAdd")
	@ResponseBody
	public Message doAdd(@ModelAttribute("workOrder") WorkOrder workOrder, HttpServletRequest request)
			throws Exception {
		User user = UserUtil.getUserFromSession();

		// 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
		// if (user == null || user.getId() == null) {
		// model.addAttribute("msg", "登录超时，请重新登录!");
		// return "login";
		// }
		Message message = new Message();
		workOrder.setUser_id(user.getId());
		workOrder.setUser_name(user.getRealName());
		workOrder.setApplyUserId(user.getId());
		workOrder.setApplyUser(user.getRealName());
		workOrder.setApplyDate(new Date());
		workOrder.setStatus(BaseVO.PENDING);
		workOrder.setBusinessType(BaseVO.WORKORDER);
		Domain domain = domainService.getDomainById(workOrder.getDomain().getId());
		Set<Project> domainSet = domain.getProjects();
		for (Project project : domainSet) {
			logger.info(project.getName());
		}
		workOrder.setTitle(domain.getName() + "(" + user.getRealName() + ")");
		workOrder.setDomain(domain);
		this.workOrderService.doAdd(workOrder);
		String businessKey = workOrder.getId().toString();
		workOrder.setBusinessKey(businessKey);
		workOrder.setWebPath(request.getServletContext().getRealPath(""));
		if(workOrder.getAccessory()!=null&&workOrder.getAccessory().getId()!=null) {
			workOrder.setAccessory(accessoryService.findById(workOrder.getAccessory().getId()));
		}else {
			workOrder.setAccessory(null);
		}
		try {
			String processInstanceId = this.processService.startWordOrder(workOrder);
			message.setStatus(Boolean.TRUE);
			message.setMessage("工单流程已启动，流程ID：" + processInstanceId);
			logger.info("processInstanceId: " + processInstanceId);
		} catch (ActivitiException e) {
			message.setStatus(Boolean.FALSE);
			if (e.getMessage().indexOf("no processes deployed with key") != -1) {
				logger.warn("没有部署流程!", e);
				message.setMessage("没有部署流程，请联系系统管理员，在[流程定义]中部署相应流程文件！");
			} else {
				logger.error("启动请假流程失败：", e);
				message.setMessage("启动请假流程失败，系统内部错误！");
			}
			throw e;
		} catch (Exception e) {
			logger.error("启动工单流程失败：", e);
			message.setStatus(Boolean.FALSE);
			message.setMessage("启动工单流程失败，系统内部错误！");
			throw e;
		}
		return message;
	}

	/**
	 * url路由
	 * 
	 * @param taskId
	 * @param model
	 * @param redirect
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/transferAction/{taskId}")
	public String transferAction(@PathVariable("taskId") String taskId, Model model, RedirectAttributes redirect)
			throws Exception {
		redirect.addFlashAttribute("taskId", taskId);
		// 获取当前任务节点 Form KEY
		TaskFormData formData = formService.getTaskFormData(taskId);
		String formKey = formData.getFormKey();
		return "redirect:" + formKey;
	}

	/**
	 * 业务部门审核数据显示
	 * 
	 * @param taskId
	 * @param model
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping("/businessAudit")
	public String businessAudit(@ModelAttribute("taskId") String taskId, Model model)
			throws NumberFormatException, Exception {
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		// 根据任务查询流程实例
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance pi = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		WorkOrder workOrder = (WorkOrder) this.runtimeService.getVariable(pi.getId(), "entity");
		// vacation.setTask(task);
		// vacation.setProcessInstanceId(processInstanceId);
		List<CommentVO> commentList = this.processService.getComments(processInstanceId);

		// 获取当前任务节点 Form KEY
		TaskFormData formData = formService.getTaskFormData(taskId);
		List<Domain> domainList = domainService.findByOnline();
		String formKey = formData.getFormKey();
		String result = "workOrder/business_audit";
		model.addAttribute("formKey", formKey);
		model.addAttribute("workOrder", workOrder);
		model.addAttribute("commentList", commentList);
		model.addAttribute("domainList", domainList);
		return result;
	}

	/**
	 * 业务部门审批
	 * 
	 * @param content
	 * @param completeFlag
	 * @param taskId
	 * @param redirectAttributes
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:workOrder:complate") // 数据库中权限字符串为user:*:complate， 通配符*匹配到vacation所以有权限操作
	@RequestMapping("/complate/{taskId}")
	@ResponseBody
	public Message complate(@RequestParam("workOrderId") Integer workOrderId,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "completeFlag", required = false) Boolean completeFlag,
			@PathVariable("taskId") String taskId, RedirectAttributes redirectAttributes,
			@ModelAttribute("workOrder") WorkOrder workOrder) throws Exception {
		User user = UserUtil.getUserFromSession();
		Message message = new Message();
		boolean isUpdateProject = false;
		try {
			Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId = task.getProcessInstanceId();
			// WorkOrder workOrder = this.workOrderService.findById(workOrderId);
			WorkOrder baseWorkOrder = (WorkOrder) this.runtimeService.getVariable(processInstanceId, "entity");
			Map<String, Object> variables = new HashMap<String, Object>();
			baseWorkOrder.setProcInstId(processInstanceId);
			String taskDefKey = task.getTaskDefinitionKey();
			if ("businessAudit".equals(taskDefKey)) { // 申请人部门审核
				variables.put("isPass", completeFlag);
				baseWorkOrder.setBusinessAuditUserId(user.getId());
				baseWorkOrder.setBusinessAuditUser(user.getRealName());
				baseWorkOrder.setBusinessAuditDate(new Date());
				// if(!completeFlag){
				// baseWorkOrder.setTitle(baseWorkOrder.getUser_name()+" 的工单申请失败,需修改后重新提交！");
				// //baseWorkOrder.setStatus(BaseVO.APPROVAL_FAILED);
				// }else {
				// baseWorkOrder.setTitle(baseWorkOrder.getUser_name()+" 的工单申请！");
				// }
				if (completeFlag) {
					// 此处需要修改，不能根据人来判断审批是否结束。应该根据流程实例id(processInstanceId)来判定。
					// 判断指定ID的实例是否存在，如果结果为空，则代表流程结束，实例已被删除(移到历史库中)
					ProcessInstance pi = this.runtimeService.createProcessInstanceQuery()
							.processInstanceId(processInstanceId).singleResult();
					if (BeanUtils.isBlank(pi)) {
						baseWorkOrder.setStatus(BaseVO.APPROVAL_SUCCESS);
					}
				}
			} else if ("businessUpdate".equals(taskDefKey)) { // 申请人修改
				// baseWorkOrder.setProject(projectService.getProjectById(
				// workOrder.getProject().getId()));
				Domain domain = domainService.getDomainById(workOrder.getDomain().getId());
				Set<Project> domainSet = domain.getProjects();
				for (Project project : domainSet) {
					logger.info(project.getName());
				}
				baseWorkOrder.setType(workOrder.getType());
				baseWorkOrder.setDomain(domain);
				baseWorkOrder.setTitle(domain.getName() + "(" + user.getRealName() + ")");
				baseWorkOrder.setCoder(workOrder.getCoder());
				baseWorkOrder.setCoderId(workOrder.getCoderId());
				baseWorkOrder.setDevelopExplain(workOrder.getDevelopExplain());
				baseWorkOrder.setApplyDate(new Date());
				baseWorkOrder.setStatus(BaseVO.PENDING);
				baseWorkOrder.setPriority(workOrder.getPriority());
				if(workOrder.getAccessory()!=null&&workOrder.getAccessory().getId()!=null) {
					baseWorkOrder.setAccessory(accessoryService.findById(workOrder.getAccessory().getId()));
				}else {
					baseWorkOrder.setAccessory(null);
				}
				baseWorkOrder.setTestFlag(workOrder.getTestFlag());
				variables.put("coderId", workOrder.getCoderId().toString());				
				variables.put("priority", workOrder.getPriority());
			} else if ("coder".equals(taskDefKey) || "coderUpdate".equals(taskDefKey)) { // 开发人员录入 coderUpdate
				variables.put("coderId", user.getId().toString());
				baseWorkOrder.setType(workOrder.getType());
				baseWorkOrder.setProject(projectService.getProjectById(workOrder.getProject().getId()));
				baseWorkOrder.setDevelopExplain(workOrder.getDevelopExplain());
				baseWorkOrder.setCoder(user.getRealName());
				baseWorkOrder.setCoderId(user.getId());
				baseWorkOrder.setCoderSvn(baseWorkOrder.getProject().getCoderSvn());
				baseWorkOrder.setCoderVersion(workOrder.getCoderVersion());
				baseWorkOrder.setCoderDate(new Date());
				baseWorkOrder.setTestFlag(workOrder.getTestFlag());
				if(workOrder.getTestFlag()!=0) {
					baseWorkOrder.setTestDesc("免测试");
				}
				if(workOrder.getScript()!=null&&workOrder.getScript().getId()!=null) {
					baseWorkOrder.setScript(accessoryService.findById(workOrder.getScript().getId()));
				}else {
					baseWorkOrder.setScript(null);
				}
				variables.put("isTest", workOrder.getTestFlag()==0?true:false);
			} else if ("coderAudit".equals(taskDefKey)) { // 开发部门审核
				variables.put("isPass", completeFlag);
				baseWorkOrder.setCoderAudit(user.getRealName());
				baseWorkOrder.setCoderAuditDate(new Date());
				baseWorkOrder.setCoderAuditId(user.getId());
				baseWorkOrder.setCoderSvn(workOrder.getCoderSvn());
				// if(!completeFlag){
				// baseWorkOrder.setTitle(baseWorkOrder.getUser_name()+"
				// 的工单申请开发审核失败,需修改后重新提交！");
				// //baseWorkOrder.setStatus(BaseVO.APPROVAL_FAILED);
				// }else {
				// baseWorkOrder.setTitle(baseWorkOrder.getUser_name()+" 的工单申请！");
				// //baseWorkOrder.setStatus(BaseVO.PENDING);
				// }
			} else if ("tester".equals(taskDefKey)) { // 测试
				variables.put("testerId", user.getId().toString());
				variables.put("isPass", completeFlag);
				baseWorkOrder.setTester(user.getRealName());
				baseWorkOrder.setTesterId(user.getId());
				baseWorkOrder.setTesterDate(new Date());
				baseWorkOrder.setTestDesc(workOrder.getTestDesc());
			} else if ("commiter".equals(taskDefKey)) { // 入库
				variables.put("commiterId", user.getId().toString());
				variables.put("isPass", completeFlag);
				baseWorkOrder.setCommiter(user.getRealName());
				baseWorkOrder.setCommiterId(user.getId());
				baseWorkOrder.setCommiterDate(new Date());
				baseWorkOrder.setTestSvn(baseWorkOrder.getProject().getTesterSvn());
				baseWorkOrder.setTestVersion(workOrder.getTestVersion());
				baseWorkOrder.setTestDesc(workOrder.getTestDesc());
				baseWorkOrder.setProjectVersion(generateVersion(baseWorkOrder.getProject(), baseWorkOrder.getType()));
				isUpdateProject = true;
			} else if ("testerAudit".equals(taskDefKey)) { // 测试部门审核
				variables.put("isPass", completeFlag);
				baseWorkOrder.setTesterAudit(user.getRealName());
				baseWorkOrder.setTesterAuditId(user.getId());
				baseWorkOrder.setTestAuditDate(new Date());
				baseWorkOrder.setTestSvn(workOrder.getTestSvn());
				// if(!completeFlag){
				// baseWorkOrder.setTitle(baseWorkOrder.getUser_name()+"
				// 的工单申请,测试部门审核失败,需修改后重新提交！");
				// //baseWorkOrder.setStatus(BaseVO.APPROVAL_FAILED);
				// }else {
				// baseWorkOrder.setTitle(baseWorkOrder.getUser_name()+" 的工单申请！");
				// //baseWorkOrder.setStatus(BaseVO.PENDING);
				// }
			} else if ("webmaster".equals(taskDefKey)) { // 运维
				variables.put("isPass", completeFlag);
				baseWorkOrder.setWebMaster(user.getRealName());
				baseWorkOrder.setWebMasterId(user.getId());
				baseWorkOrder.setDeployDate(new Date());
				// if(!completeFlag) {
				// baseWorkOrder.setTitle(baseWorkOrder.getUser_name()+" 的工单部署失败回滚,需修改后重新提交！");
				// }else {
				// baseWorkOrder.setTitle(baseWorkOrder.getUser_name()+" 的工单申请！");
				// }
			} else if ("webmasterAudit".equals(taskDefKey)) { // 运维部门审核
				baseWorkOrder.setWebMasterAudit(user.getRealName());
				baseWorkOrder.setWebMasterAuditId(user.getId());
				baseWorkOrder.setWebMasterAuditDate(new Date());
				content = "同意";
			} else if ("applyConfirm".equals(taskDefKey)) { // 申请人确认
				variables.put("isPass", completeFlag);
				baseWorkOrder.setVerifyDate(new Date());
				if (completeFlag) {
					baseWorkOrder.setRollbackFlag(BaseVO.WORKORDER_NORMAL);
					baseWorkOrder.setStatus(BaseVO.APPROVAL_SUCCESS);
				} else {
					baseWorkOrder.setRollbackFlag(BaseVO.WORKORDER_ROLLBACK);
					baseWorkOrder.setRollbackReason(workOrder.getRollbackReason());
				}
			} else if ("versionRollback".equals(taskDefKey)) { // 版本回滚
				baseWorkOrder.setRollbackReason(workOrder.getRollbackReason());
				baseWorkOrder.setRollbackVersion(workOrder.getRollbackVersion());
				baseWorkOrder.setStatus(BaseVO.APPROVAL_SUCCESS);
			}
			variables.put("entity", baseWorkOrder);
			variables.put("lastId", user.getId());
			// 完成任务
			this.processService.complete(taskId, content, user.getId().toString(), variables);
			this.workOrderService.doUpdate(baseWorkOrder, isUpdateProject);
			message.setStatus(Boolean.TRUE);
			message.setMessage("任务办理完成！");
		} catch (ActivitiObjectNotFoundException e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("此任务不存在，请联系管理员！");
			throw e;
		} catch (ActivitiException e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("此任务正在协办，您不能办理此任务！");
			throw e;
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("任务办理失败，请联系管理员！");
			throw e;
		}
		return message;
	}

	/**
	 * 申请人修改
	 * 
	 * @param taskId
	 * @param model
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping("/businessUpdate")
	public String businessUpdate(@ModelAttribute("taskId") String taskId, Model model)
			throws NumberFormatException, Exception {
		String result = "workOrder/edit_workOrder";
		searchTask(taskId, model);
		return result;
	}

	/**
	 * 开发录入/修改
	 * 
	 * @param taskId
	 * @param model
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value = { "/coder", "/coderUpdate" })
	public String coder(@ModelAttribute("taskId") String taskId, Model model) throws NumberFormatException, Exception {
		String result = "workOrder/form_coder";
		searchTask(taskId, model);
		return result;
	}

	/**
	 * 开发审核
	 * 
	 * @param taskId
	 * @param model
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value = { "/coderAudit" })
	public String coderAudit(@ModelAttribute("taskId") String taskId, Model model)
			throws NumberFormatException, Exception {
		String result = "workOrder/form_coder_audit";
		searchTask(taskId, model);
		return result;
	}

	/**
	 * 测试录入
	 * 
	 * @param taskId
	 * @param model
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value = { "/tester", "testerUpdate" })
	public String tester(@ModelAttribute("taskId") String taskId, Model model) throws NumberFormatException, Exception {
		String result = "workOrder/form_tester";
		searchTask(taskId, model);
		return result;
	}

	/**
	 * 入库
	 * 
	 * @param taskId
	 * @param model
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value = { "/commiter" })
	public String commiter(@ModelAttribute("taskId") String taskId, Model model)
			throws NumberFormatException, Exception {
		String result = "workOrder/form_commiter";
		WorkOrder workOrder = searchTask(taskId, model);
		Project project = workOrder.getProject();
		if (project != null) {
			String version = generateVersion(project, workOrder.getType());
			model.addAttribute("version", version + " " + StringUtils.trimToNull(workOrder.getDevelopExplain()));
		}
		return result;
	}

	/**
	 * 测试审核
	 * 
	 * @param taskId
	 * @param model
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value = { "/testerAudit" })
	public String testerAudit(@ModelAttribute("taskId") String taskId, Model model)
			throws NumberFormatException, Exception {
		String result = "workOrder/form_tester_audit";
		searchTask(taskId, model);
		return result;
	}

	/**
	 * 运维部署
	 * 
	 * @param taskId
	 * @param model
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value = { "/webmaster" })
	public String webmaster(@ModelAttribute("taskId") String taskId, Model model)
			throws NumberFormatException, Exception {
		String result = "workOrder/form_webmaster";
		searchTask(taskId, model);
		return result;
	}

	/**
	 * 运维部门审核
	 * 
	 * @param taskId
	 * @param model
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value = { "/webmasterAudit" })
	public String webmasterAudit(@ModelAttribute("taskId") String taskId, Model model)
			throws NumberFormatException, Exception {
		String result = "workOrder/form_webmaster_audit";
		searchTask(taskId, model);
		return result;
	}

	/**
	 * 发起人确认
	 * 
	 * @param taskId
	 * @param model
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value = { "/workOrderVerify" })
	public String workOrderVerify(@ModelAttribute("taskId") String taskId, Model model)
			throws NumberFormatException, Exception {
		String result = "workOrder/form_verify";
		searchTask(taskId, model);
		return result;
	}

	/**
	 * 回滚
	 * 
	 * @param taskId
	 * @param model
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@RequestMapping(value = { "/rollback" })
	public String rollback(@ModelAttribute("taskId") String taskId, Model model)
			throws NumberFormatException, Exception {
		String result = "workOrder/form_rollback";
		searchTask(taskId, model);
		return result;
	}

	private WorkOrder searchTask(String taskId, Model model) throws Exception {
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		// 根据任务查询流程实例
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance pi = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		WorkOrder workOrder = (WorkOrder) this.runtimeService.getVariable(pi.getId(), "entity");
		System.out.println(workOrder.getDomain().getProjects().size());
		if (workOrder.getDomain().getProjects() == null || workOrder.getDomain().getProjects().size() < 1) {
			Domain domain = domainService.getDomainById(workOrder.getDomain().getId());
			if (domain != null) {
				workOrder.getDomain().setProjects(domain.getProjects());
			}
		}
		List<CommentVO> commentList = this.processService.getComments(processInstanceId);
		// 获取当前任务节点 Form KEY
		TaskFormData formData = formService.getTaskFormData(taskId);
		List<Domain> domainList = domainService.findByOnline();
		String formKey = formData.getFormKey();
		model.addAttribute("formKey", formKey);
		model.addAttribute("workOrder", workOrder);
		model.addAttribute("commentList", commentList);
		model.addAttribute("domainList", domainList);
		return workOrder;
	}

	/**
	 * 调整请假申请
	 * 
	 * @param vacation
	 * @param taskId
	 * @param processInstanceId
	 * @param reApply
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:vacation:modify")
	@RequestMapping(value = "/modifyVacation/{taskId}", method = RequestMethod.POST)
	@ResponseBody
	public Message modifyVacation(@ModelAttribute("vacation") Vacation vacation, @PathVariable("taskId") String taskId,
			@RequestParam("processInstanceId") String processInstanceId, @RequestParam("reApply") Boolean reApply)
			throws Exception {

		User user = UserUtil.getUserFromSession();
		Message message = new Message();

		Map<String, Object> variables = new HashMap<String, Object>();
		vacation.setUserId(user.getId());
		vacation.setUser_name(user.getName());
		vacation.setBusinessType(BaseVO.VACATION);
		vacation.setApplyDate(new Date());
		vacation.setBusinessKey(vacation.getId().toString());
		vacation.setProcessInstanceId(processInstanceId);
		if (reApply) {
			// 修改请假申请
			vacation.setTitle(user.getName() + " 的请假申请！");
			vacation.setStatus(BaseVO.PENDING);
			// 由userTask自动分配审批权限
			// if(vacation.getDays() <= 3){
			// variables.put("auditGroup", "manager");
			// }else{
			// variables.put("auditGroup", "director");
			// }
			message.setStatus(Boolean.TRUE);
			message.setMessage("任务办理完成，请假申请已重新提交！");
		} else {
			vacation.setTitle(user.getName() + " 的请假申请已取消！");
			vacation.setStatus(BaseVO.APPROVAL_FAILED);
			message.setStatus(Boolean.TRUE);
			message.setMessage("任务办理完成，已经取消您的请假申请！");
		}
		try {
			this.vacationService.doUpdate(vacation);
			variables.put("entity", vacation);
			variables.put("reApply", reApply);
			this.processService.complete(taskId, "取消申请", user.getId().toString(), variables);

		} catch (ActivitiObjectNotFoundException e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("此任务不存在，请联系管理员！");
			throw e;
		} catch (ActivitiException e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("此任务正在协办，您不能办理此任务！");
			throw e;
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("任务办理失败，请联系管理员！");
			throw e;
		}

		return message;
	}

	/**
	 * 详细信息
	 * 
	 * @param id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:vacation:details")
	@RequestMapping(value = "/details/{id}")
	public String details(@PathVariable("id") Integer id, Model model) throws Exception {
		Vacation vacation = this.vacationService.findById(id);
		model.addAttribute("vacation", vacation);
		return "/vacation/details_vacation";
	}

	@SuppressWarnings("serial")
	@RequestMapping(value = "/downloadWord")
	public void downloadWord(HttpServletRequest request, HttpServletResponse response, Integer id) throws Exception {
		WorkOrder workOrder = workOrderService.findById(id);
		// 获取应用的根路径
		String servletContextRealPath = request.getServletContext().getRealPath("");
		XWPFTemplate template = workOrderService.generatePrintWorkorder(servletContextRealPath, workOrder);
		// 输出 word 内容文件流，提供下载
		response.reset();
		response.setContentType("application/x-msdownload");
		// 随机生成一个文件名
		UUID randomUUID = UUID.randomUUID();
		String attachmentName = randomUUID.toString();
		response.addHeader("Content-Disposition", "attachment; filename=\"" + attachmentName + ".doc\"");
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		ServletOutputStream servletOS = response.getOutputStream();
		template.write(ostream);
		servletOS.write(ostream.toByteArray());
		servletOS.flush();
		servletOS.close();
		ostream.close();
		template.close();
	}

	@SuppressWarnings("serial")
	@RequestMapping(value = "/saveExcel")
	public void saveExcel(HttpServletRequest request, HttpServletResponse response, String applyUser,
			String projectName, String type, String beginDate, String endDate) throws Exception {
		String hql = "select w from WorkOrder w where w.status='" + BaseVO.APPROVAL_SUCCESS + "'";
		String sort = "applyDate";
		String order = "desc";
		List<Object> values = new ArrayList<Object>();
		if (StringUtils.isNotBlank(projectName)) {
			hql += " and project.name like ? ";
			values.add("%" + projectName + "%");
		}
		if (StringUtils.isNotBlank(type)) {
			hql += " and type=? ";
			values.add(type);
		}
		if (StringUtils.isNotBlank(applyUser)) {
			hql += " and applyUser=? ";
			values.add(applyUser);
		}
		if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
			hql += " and testerDate >= ?  and testerDate<=? ";
			values.add(DateUtil.StringToDate(beginDate, "yyyy-MM-dd"));
			values.add(DateUtil.StringToDate(endDate, "yyyy-MM-dd"));
		}
		List<WorkOrder> workOrders = workOrderService.getWorkOrderList(hql, sort, order, values.toArray());
		PoiExcelExport pee = new PoiExcelExport(response, "软件版本发布工单", "sheet1");

		String titleColumn[] = { "id", "domainDomain", "domainName", "projectName", "commiterDate", "coder",
				"applyUser", "tester", "commiter", "type", "home", "area", "priorityStr", "coderSvn", "coderVersion",
				"describe", "rollbackDesc", "rollbackVersion", "rollbackReason" };
		String titleName[] = { "工单编号", "域名", "域名称", "项目名称", "入库时间", "开发人员", "申请人", "测试人员", "入库人员", "类别", "项目归属", "应用地区",
				"优先级", "SVN地址", "更新版本", "修改内容", "是否回滚", "回滚版本", "回滚原因" };
		int titleSize[] = { 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13 };
		pee.wirteExcel(titleColumn, titleName, titleSize, workOrders);
	}

	@RequestMapping(value = "/upload")
	@ResponseBody
	public Message deploy(@Value("#{APP_PROPERTIES['upload.path']}") String uploadPath,
			@RequestParam(value = "file", required = false) MultipartFile file,int type) {
		User user = UserUtil.getUserFromSession();
		Message message = new Message();
		String origFileName = file.getOriginalFilename();
		try {
			String relativeFolderName = DateUtil.DateToString(new Date(), "yyyyMM");
			String folderName = uploadPath + File.separator + relativeFolderName;
			File folderDic = new File(folderName);
			if (!folderDic.exists())
				folderDic.mkdirs();

			String newFileName = System.currentTimeMillis() + "." + FilenameUtils.getExtension(origFileName);
			File destFile = new File(folderName, newFileName);
			file.transferTo(destFile);
			
			Accessory accessory = new Accessory();
			accessory.setName(origFileName);
			accessory.setPath(relativeFolderName+File.separator+newFileName);
			accessory.setType(type);
			accessory.setCreateTime(new Date());
			accessory.setCreater(user.getId());
			accessoryService.doAdd(accessory);
			message.setMemo(accessory.getId().toString());
			message.setTitle(origFileName);
			message.setStatus(Boolean.TRUE);
			message.setMessage("上传成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("上传失败！");
			logger.error("error on deploy process, because of file input stream", e);
		}
		return message;
	}
	
	@RequestMapping(value = "/download")
	public ResponseEntity<byte[]> download(@Value("#{APP_PROPERTIES['upload.path']}") String uploadPath,Integer id) throws Exception{
		Accessory accessory = accessoryService.findById(id);
		File file = new File(uploadPath+File.separator+accessory.getPath());		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", accessory.getName());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);				
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers,HttpStatus.CREATED);
	}
	/**
	 * 未使用方法
	 * @param response
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delAccessory")
	@ResponseBody
	public Message delAccessory(HttpServletResponse response,Integer id) throws Exception {
		Message message = new Message();
		try {
			List<WorkOrder> workorderList = workOrderService.getWorkOrderByAccessoryId(id);
			
//			Accessory accessory = new Accessory();
//			accessory.setId(id);
//			accessoryService.doDelete(accessory);				
			message.setStatus(Boolean.TRUE);
			message.setMessage("删除成功！");
		}catch(Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("删除失败！");
			logger.error("err----------------", e);
		}
		return message;
	}

	@RequestMapping(value = "/showDevelopExplain/{id}")
	public String showDevelopExplain(@PathVariable("id") Integer id, Model model) throws Exception {
		WorkOrder workorder = workOrderService.findById(id);
		model.addAttribute("developExplain", workorder.getDevelopExplain());
		return "workOrder/show_develop_explain";
	}
	
	private String generateVersion(Project project, String type) {
		if (project == null)
			return "";
		StringBuilder sb = new StringBuilder();
		sb.append(project.getVersionPrefix()).append(".");
		if ("非程序类修改".equals(type)) {
			project.setVersionNoCode(project.getVersionNoCode() + 1);
		} else {
			project.setVersionCode(project.getVersionCode() + 1);
		}
		sb.append(project.getVersionCode()).append(".").append(project.getVersionNoCode());
		return sb.toString();
	}
}
