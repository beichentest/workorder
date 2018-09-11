package com.zml.oa.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zml.oa.entity.Datagrid;
import com.zml.oa.entity.Domain;
import com.zml.oa.entity.Message;
import com.zml.oa.entity.Project;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IDomainService;
import com.zml.oa.service.IProjectService;
import com.zml.oa.util.DateEditor;
/**
 * 
* @ClassName: ProjectAction  
* @Description: TODO(这里用一句话描述这个类的作用)  
* @author Administrator  
* @date 2018年6月29日  
*
 */
@Controller
@RequestMapping("/domainAction")
public class DomainAction {
	private static final Logger logger = Logger.getLogger(DomainAction.class);
	
	@Autowired
	private IDomainService domainService;
	
	@Autowired
	private IProjectService projectService;
	
//	@RequiresPermissions("admin:*")
//	@RequestMapping("/toList_page")
//	public String userList_page(HttpServletRequest request, Model model) throws Exception{
//		List<User> listUser = this.userService.getUserList_page();
//		Pagination pagination = PaginationThreadUtils.get();
////		pagination.processTotalPage();
//		model.addAttribute("page", pagination.getPageStr());
//		model.addAttribute("listUser", listUser);
//		return "user/list_user";
//	}
	
	@RequestMapping(value = "/domainDatagrid")
	public String toList_page() throws Exception{
		return "domain/list_domain";
	}
	
	/**
	 * 
	 * @param page 当前第几页
	 * @param rows 每页显示记录数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toList")
	@ResponseBody
	public Datagrid<Object> userList(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows,String sort,String order,String name,String domain) throws Exception{
		sort = com.zml.oa.util.StringUtils.camel2Underline(sort);
		String sql = "select d from Domain d  left outer join fetch d.projects where 1=1 ";
		Page<Domain> p = new Page<Domain>(page, rows);		
		List<Object> values = new ArrayList<Object>();
		if(StringUtils.isNotBlank(name)) {
			sql +=" and d.name like ? ";
			values.add("%"+name+"%");
		}
		if(StringUtils.isNotBlank(domain)) {
			sql +=" and d.domain like ? ";
			values.add("%"+domain+"%");
		}
		List<Domain> domainList = this.domainService.getDomainList(sql, p, values.toArray(), sort, order);
		List<Object> jsonList=new ArrayList<Object>(); 
		for (Domain dom : domainList) {
			jsonList.add(BeanUtils.describe(dom));
		}
		return new Datagrid<Object>(p.getTotal(), jsonList);
	}
	
	//此方法没用到，用Shiro提供的授权和认证服务
//	@RequestMapping(value = "/login", method = RequestMethod.POST)
//	public String login(@RequestParam("name")String name, @RequestParam("passwd")String passwd, HttpServletRequest request, Model model) throws Exception{
//		logger.info("login - username=" + name + ", password=" + passwd);
//		Session session = SecurityUtils.getSubject().getSession();
//		User user = userService.getUserByName(name);
//		if(!BeanUtils.isBlank(user)){
//			if(passwd.equals(user.getPasswd())){
//				UserUtil.saveUserToSession(session, user);
//				return "index";
//			}else{
//				model.addAttribute("msg", "密码不正确");
//				logger.info("密码不正确");
//				return "login";
//			}
//		}else{
//			model.addAttribute("msg", "用户名不存在");
//			logger.info(name+" 用户名不存在");
//			return "login";
//		}
//	}
//
	@RequestMapping(value = "/toAdd")
	public String toAdd() throws Exception{
//		List<Group> list = this.groupService.getGroupList();
//		model.addAttribute("groupList", list);
		return "domain/add_domain";
	}

	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	@ResponseBody
	public Message doAdd(@ModelAttribute("domain") Domain domain) throws Exception{		
		domainService.doAdd(domain);
		return new Message(Boolean.TRUE, "添加成功！");
	}
	@RequestMapping(value="/selectProject/{id}")
	public String selectProject(@PathVariable("id") Integer id,Model model,String sort,String order)throws Exception{		
		model.addAttribute("domainId", id);
		return "domain/select_project";
	}
	@RequestMapping(value = "/domainProjectList")
	@ResponseBody
	public List<Object> domainProjectList(String sort,String order,Integer domainId) throws Exception{
		String hql = "select d from Domain d  left outer join fetch d.projects where d.id=?";
		List<Object> values = new ArrayList<Object>();
		values.add(domainId);		
		List<Domain> domainList = this.domainService.getDomainList(hql, values.toArray(), sort, order);
		if(domainList!=null&&domainList.size()>0) {
			List<Object> jsonList=new ArrayList<Object>();
			for (Project project : domainList.get(0).getProjects()) {
				if(project.getStatus().equals("0"))
					jsonList.add(BeanUtils.describe(project));
			}
			return jsonList;
		}
		return null;
	}
	@RequestMapping(value = "/rmProject")
	@ResponseBody
	public Message rmProject(Integer projectId, Integer domainId) throws Exception {
		Project project = this.projectService.getProjectById(projectId);
		String hql = "select d from Domain d  left outer join fetch d.projects where d.id=?";
		Page<Domain> p = new Page<Domain>(0, 9999);
		List<Object> values = new ArrayList<Object>();
		values.add(domainId);
		List<Domain> domainList = this.domainService.getDomainList(hql,p, values.toArray(), null,null);
		if(domainList!=null&&domainList.size()>0) {
			domainList.get(0).getProjects().remove(project);
			this.domainService.doUpdate(domainList.get(0));
		}
		return new Message(Boolean.TRUE, "移除成功！");
	}
	
	@RequestMapping(value = "/addProject")
	@ResponseBody
	public Message addProject(Integer projectId, Integer domainId) throws Exception {
		Project project = this.projectService.getProjectById(projectId);
		String hql = "select d from Domain d  left outer join fetch d.projects where d.id=?";
		Page<Domain> p = new Page<Domain>(0, 9999);
		List<Object> values = new ArrayList<Object>();
		values.add(domainId);
		List<Domain> domainList = this.domainService.getDomainList(hql,p, values.toArray(), null,null);
		if(domainList!=null&&domainList.size()>0) {
			if(domainList.get(0).getProjects()!=null) {
				domainList.get(0).getProjects().add(project);
			}else {
				Set<Project> set = new HashSet<Project>();
				set.add(project);
				domainList.get(0).setProjects(set);
			}
			try {
				this.domainService.doUpdate(domainList.get(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Message(Boolean.TRUE, "添加成功！");
	}
	
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Message delete(Integer id,String status) throws Exception{
		if(!com.zml.oa.util.BeanUtils.isBlank(id)){
			domainService.doDelete(id,status);			
			return new Message(Boolean.TRUE, "修改成功！");
		}else{
			return new Message(Boolean.FALSE, "修改失败！ID为空！");
		}
	}
	
	
	@RequestMapping(value = "/toUpdate/{id}")
	public String toUpdate(@PathVariable("id") Integer id,Model model) throws Exception{
		Domain domain = domainService.getDomainById(id);
		model.addAttribute("domain", domain);
		return "domain/update_domain";
	}
	
	@RequestMapping(value = "/doUpdate")
	@ResponseBody
	public Message doUpdate(@ModelAttribute("domain") Domain domain) throws Exception{
		Message message = new Message();
		String hql = "select d from Domain d  left outer join fetch d.projects where d.id=?";
		Page<Domain> p = new Page<Domain>(0, 9999);
		List<Object> values = new ArrayList<Object>();
		values.add(domain.getId());
		List<Domain> domainList = this.domainService.getDomainList(hql,p, values.toArray(), null,null);
		if(domainList!=null&&domainList.size()>0) {
			Domain domainOrig = domainList.get(0);
			domainOrig.setName(domain.getName());
			domainOrig.setDomain(domain.getDomain());
			domainOrig.setSubjection(domain.getSubjection());
			domainOrig.setArea(domain.getArea());
			domainOrig.setManager(domain.getManager());
			domainOrig.setReleaseDate(domain.getReleaseDate());
			domainOrig.setMemo(domain.getMemo());
			domainOrig.setMotorRoom(domain.getMotorRoom());
			domainService.doUpdate(domainOrig);
		}		
		message.setStatus(Boolean.TRUE);
		message.setMessage("修改成功！");
		return message;
	}
    @InitBinder  
    protected void initBinder(HttpServletRequest request,  
                                  ServletRequestDataBinder binder) throws Exception {  
        //对于需要转换为Date类型的属性，使用DateEditor进行处理  
        binder.registerCustomEditor(Date.class, new DateEditor());  
    }  
	
}
