package com.zml.oa.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import com.zml.oa.util.DateEditor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.zml.oa.entity.Message;
import com.zml.oa.entity.Project;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IProjectService;
/**
 * 
* @ClassName: ProjectAction  
* @Description: TODO(这里用一句话描述这个类的作用)  
* @author Administrator  
* @date 2018年6月29日  
*
 */
@Controller
@RequestMapping("/projectAction")
public class ProjectAction {
	private static final Logger logger = Logger.getLogger(ProjectAction.class);
	
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
	
	@RequestMapping(value = "/projectDatagrid")
	public String toList_page() throws Exception{
		return "project/list_project";
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
	public Datagrid<Object> userList(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows,String sort,String order,String name) throws Exception{
		String sql = "select p from Project p where status='0' ";
		Page<Project> p = new Page<Project>(page, rows);		
		List<Object> values = new ArrayList<Object>();
		if(StringUtils.isNotBlank(name)) {
			sql +=" and name like ?";
			values.add("%"+name+"%");
		}
		List<Project> projectList = this.projectService.getProjectList(sql, p, values.toArray(), sort, order);
		List<Object> jsonList=new ArrayList<Object>(); 
		for (Project project : projectList) {
			jsonList.add(BeanUtils.describe(project));
		}
		return new Datagrid<Object>(p.getTotal(), jsonList);
	}
	
	@RequestMapping("/toComboGridList")
	@ResponseBody
	public List<Object> comboGridList(String sort,String order,String q) throws Exception{
		String hql = "select p from Project p where status='0' ";
		List<Object> values = new ArrayList<Object>();
		if(StringUtils.isNotBlank(q)) {
			hql +=" and name like ?";
			values.add("%"+q+"%");
		}
		List<Project> projectList = this.projectService.getProjectList(hql, values.toArray(), sort, order);
		List<Object> jsonList=new ArrayList<Object>();
		for (Project project : projectList) {
			jsonList.add(BeanUtils.describe(project));
		}
		return jsonList;
	}
	
	@RequestMapping(value = "/toAdd")
	public String toAdd() throws Exception{
//		List<Group> list = this.groupService.getGroupList();
//		model.addAttribute("groupList", list);
		return "project/add_project";
	}

	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	@ResponseBody
	public Message doAdd(@ModelAttribute("project") Project project) throws Exception{		
		projectService.doAdd(project);
		return new Message(Boolean.TRUE, "添加成功！");
	}
	
	@RequestMapping(value = "/delete/{id}")
	@ResponseBody
	public Message delete(@PathVariable("id") Integer id) throws Exception{
		if(!com.zml.oa.util.BeanUtils.isBlank(id)){
			projectService.doDelete(id);			
			return new Message(Boolean.TRUE, "删除成功！");
		}else{
			return new Message(Boolean.FALSE, "删除失败！ID为空！");
		}
	}
	
	
	@RequestMapping(value = "/toUpdate/{id}")
	public String toUpdate(@PathVariable("id") Integer id,Model model) throws Exception{
		Project project = projectService.getProjectById(id);
		model.addAttribute("project", project);
		return "project/update_project";
	}
	
	@RequestMapping(value = "/doUpdate")
	@ResponseBody
	public Message doUpdate(@ModelAttribute("project") Project project) throws Exception{
		Message message = new Message();
		projectService.doUpdate(project);
		message.setStatus(Boolean.TRUE);
		message.setMessage("修改成功！");
		return message;
	}
//	
//	@RequestMapping(value = "/showUser")
//	public String showUser(Model model) throws Exception{
//		User user = UserUtil.getUserFromSession();
//		model.addAttribute("user", user);
//		return "user/show_user";
//	}
//	@RequestMapping(value = "/doUpdateUser")
//	@ResponseBody
//	public Message doUpdateUser(HttpServletRequest request) throws Exception{
//		Message message = new Message();
//		User user = UserUtil.getUserFromSession();
//		String passwd = request.getParameter("passwd");
//		user.setPasswd(passwd);
//		this.userService.doUpdate(user);
//		Subject currentUser = SecurityUtils.getSubject();
//		UserRealm ur = new UserRealm();
//		ur.clearCachedAuthenticationInfo(currentUser.getPrincipals());
//		
//		message.setStatus(Boolean.TRUE);
//		message.setMessage("修改成功！");
//		return message;
//	}
//	
//	@RequiresPermissions("admin:user:toUpdate")
//	@RequestMapping(value = "/toUpdate/{id}")
//	public String toUpdate(@PathVariable("id") Integer id,Model model) throws Exception{
////		List<Group> list = this.groupService.getGroupList();
//		User user = this.userService.getUserById(id);
////		model.addAttribute("groupList", list);
//		model.addAttribute("user", user);
//		return "user/update_user";
//	}
//	
//	@RequiresPermissions("admin:user:doUpdate")
//    @RequestMapping(value = "/doUpdate")
//    @ResponseBody
//	public Message doUpdate(HttpServletRequest request) throws Exception{
//		String id = request.getParameter("id");
//		String salt = request.getParameter("salt");
//		String name = request.getParameter("name");
//		String registerDate = request.getParameter("registerDate");
//		String passwd = request.getParameter("passwd");
//		String groupId = request.getParameter("group.id");
//		String locked = request.getParameter("locked");
//		String realName = request.getParameter("realName");
//		Message message = new Message();
//		User user = new User();
//		if(StringUtils.isNotEmpty(id)){
//			user.setId(new Integer(id));
//			user.setName(name);
//			user.setSalt(salt);
//			user.setPasswd(passwd);
//			user.setLocked(new Integer(locked));
//			user.setRealName(realName);
//			if(StringUtils.isNotEmpty(groupId)){
//				user.setGroup(new Group(new Integer(groupId)));
//			}else{
//				message.setStatus(Boolean.FALSE);
//				message.setMessage("group.id 为空！");
//			}
//			//Date date = DateUtil.StringToDate(registerDate, "yyyy-MM-dd HH:mm");
//			//user.setRegisterDate(date);
//		}else{
//			message.setStatus(Boolean.FALSE);
//			message.setMessage("userId 为空！");
//		}
//		if(message.getStatus()){
//			this.userService.doUpdate(user);
//			//清空认证缓存
//			Subject currentUser = SecurityUtils.getSubject();
//			UserRealm ur = new UserRealm();
//			ur.clearCachedAuthenticationInfo(currentUser.getPrincipals());
//			
//			message.setStatus(Boolean.TRUE);
//			message.setMessage("修改成功！");
//		}
//		return message;
//	}
//	
//	
//	
//	@RequiresPermissions("admin:*")
//	@RequestMapping(value = "/toListOnlineUser")
//	public String toOnlineUser() throws Exception{
//		return "user/online_user";
//	}
//	
//	@RequiresPermissions("admin:*")
//	@RequestMapping(value = "/listOnlineUser")
//	@ResponseBody
//    public List<Object> list(Model model) {
//        Collection<Session> sessions =  sessionDAO.getActiveSessions();
//        List<Object> jsonList=new ArrayList<Object>(); 
//        for(Session session : sessions){
//        	Map<String, Object> map=new HashMap<String, Object>();
//        	PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
//        	String userName = (String)principalCollection.getPrimaryPrincipal();
//        	Boolean forceLogout = session.getAttribute(Constants.SESSION_FORCE_LOGOUT_KEY) != null;
//        	map.put("id", session.getId());
//        	map.put("userName", userName);
//        	map.put("host", session.getHost());
//        	map.put("lastAccessTime", session.getLastAccessTime());
//        	map.put("forceLogout", forceLogout);
//        	jsonList.add(map);
//        }
//        return jsonList;
//    }
//
//	@RequiresPermissions("admin:session:forceLogout")
//    @RequestMapping("/forceLogout/{sessionId}")
//	@ResponseBody
//    public Message forceLogout(@PathVariable("sessionId") String sessionId) {
//		Message message = new Message();
//        try {
//            Session session = sessionDAO.readSession(sessionId);
//            if(session != null) {
//                session.setAttribute(Constants.SESSION_FORCE_LOGOUT_KEY, Boolean.TRUE);
//            }
//            message.setStatus(Boolean.TRUE);
//            message.setMessage("强制退出成功！");
//        } catch (Exception e) {
//        	message.setStatus(Boolean.FALSE);
//            message.setMessage("强制退出失败！");
//        }
//        return message;
//    }
//	
//	/**
//	 * 同步所有用户到activiti表
//	 * @param redirectAttributes
//	 * @return
//	 * @throws Exception
//	 */
//	@RequiresPermissions("admin:user:syncUser")
//	@RequestMapping("/syncUserToActiviti")
//	@ResponseBody
//	public Message syncUserToActiviti() throws Exception {
//		Message message = new Message();
//		try {
//			this.userService.synAllUserAndRoleToActiviti();
//			message.setStatus(Boolean.TRUE);
//            message.setMessage("同步用户信息成功！");
//		} catch (Exception e) {
//			message.setStatus(Boolean.FALSE);
//            message.setMessage("同步用户信息失败！");
//		}
//		return message;
//	}
//	
//	//如果执行删除，工作流审批中的代办任务和待签收任务将无法使用。（在act_ru_identitylink将查不到act_id_user、act_id_group和act_id_membership的信息）
//	@RequiresPermissions("admin:user:delAllIdentifyData")
//	@RequestMapping("/delAllIdentifyData")
//	public String delAllIdentifyData(RedirectAttributes redirectAttributes) throws Exception {
//		this.userService.deleteAllActivitiIdentifyData();
//		redirectAttributes.addFlashAttribute("msg", "成功删除工作流引擎Activiti的用户、角色以及关系！");
//		return "redirect:/userAction/toList_page";
//	}  
//	
//	
//	/**
//	 * 跳转选择候选人页面-easyui
//	 * @return
//	 * @throws Exception 
//	 */
//	@RequestMapping(value = "/toChooseUser")
//	public ModelAndView toChooseUser(@RequestParam("multiSelect") boolean multiSelect, @RequestParam("taskDefKey") String taskDefKey) throws Exception{
//		ModelAndView mv = new ModelAndView("bpmn/choose_user");
//		List<Group> groupList = this.groupService.getGroupList();
//		mv.addObject("taskDefKey", taskDefKey);
//		mv.addObject("multiSelect", multiSelect);
//		mv.addObject("groupList", groupList);
//		return mv;
//	}
//	
//	/**
//	 * 跳转选择任务委派人页面-easyui
//	 * @return
//	 * @throws Exception 
//	 */
//	@RequestMapping(value = "/toChooseDelegateUser")
//	public ModelAndView toChooseDelegateUser() throws Exception{
//		ModelAndView mv = new ModelAndView("task/delegate_user");
//		List<Group> groupList = this.groupService.getGroupList();
//		mv.addObject("groupList", groupList);
//		return mv;
//	}
//	
//	/**
//	 * 在tabs中根据groupId显示用户列表
//	 * @return
//	 */
//	@RequestMapping(value = "/toShowUser")
//	public ModelAndView toShowUser(
//			@RequestParam("groupId") String groupId,
//			@RequestParam("multiSelect") boolean multiSelect, 
//			@RequestParam("taskDefKey") String taskDefKey){
//		ModelAndView mv = new ModelAndView("bpmn/show_user");
//		mv.addObject("groupId", groupId);
//		mv.addObject("multiSelect", multiSelect);
//		mv.addObject("taskDefKey", taskDefKey);
//		return mv;
//	}
//	
//	/**
//	 * 在tabs中根据groupId显示用户列表
//	 * @return
//	 */
//	@RequestMapping(value = "/toShowDelegateUser")
//	public ModelAndView toShowDelegateUser(
//			@RequestParam("groupId") String groupId){
//		ModelAndView mv = new ModelAndView("task/show_user");
//		mv.addObject("groupId", groupId);
//		return mv;
//	}
//	
//	/**
//	 * 获取候选人列表 - easyui
//	 * @param page
//	 * @param rows
//	 * @param groupId
//	 * @param flag
//	 * @param key
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "/chooseUser")
//	@ResponseBody
//	public Datagrid<Object> chooseUser(
//			@RequestParam(value = "page", required = false) Integer page, 
//			@RequestParam(value = "rows", required = false) Integer rows,
//			@RequestParam(value = "groupId", required = false) String groupId) throws Exception{
//		Page<User> p = new Page<User>(page, rows);
//		if(groupId == null){
// 			this.userService.getUserList(p);
//		}else{
//			this.userService.getUserByGroupId(groupId, p);
//		}
//		List<Object> jsonList=new ArrayList<Object>(); 
//		
//		for(User user: p.getResult()){
//			Map<String, Object> map=new HashMap<String, Object>();
//			map.put("id", user.getId());
//			map.put("name", user.getName());
//			map.put("realName", user.getRealName());
//			map.put("group", user.getGroup().getName());
//			map.put("registerDate", user.getRegisterDate());
//			jsonList.add(map);
//		}
//		Datagrid<Object> dataGrid = new Datagrid<Object>(p.getTotal(), jsonList);
//		return dataGrid;
//		
//		
////		List<User> userList = new ArrayList<>();
////		if(groupId != null){
////			userList = this.userService.getUserList_page();
////		}else{
////			userList = this.userService.getUserByGroupId(groupId);
////		}
////		Pagination pagination = PaginationThreadUtils.get();
////		List<Group> groupList = this.groupService.getGroupList();
////		
////		model.addAttribute("userList", userList);
////		model.addAttribute("groupList", groupList);
////		model.addAttribute("groupId", groupId);
////		model.addAttribute("key", key);
////		model.addAttribute("flag", flag);
////		return "user/choose_user";
//	}
    @InitBinder  
    protected void initBinder(HttpServletRequest request,  
                                  ServletRequestDataBinder binder) throws Exception {  
        //对于需要转换为Date类型的属性，使用DateEditor进行处理  
        binder.registerCustomEditor(Date.class, new DateEditor());  
    }  
	
}
