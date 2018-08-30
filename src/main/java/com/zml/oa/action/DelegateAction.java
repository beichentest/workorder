package com.zml.oa.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zml.oa.entity.Delegate;
import com.zml.oa.entity.Message;
import com.zml.oa.entity.Project;
import com.zml.oa.entity.User;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IDelegateService;
import com.zml.oa.util.DateEditor;
import com.zml.oa.util.DateUtil;
import com.zml.oa.util.UserUtil;
/**
 * 
* @ClassName: DelegateAction  
* @Description: 流程代理  
* @author Administrator  
* @date 2018年8月29日  
*
 */
@Controller
@RequestMapping("/delegateAction")
public class DelegateAction {
	private static final Logger logger = Logger.getLogger(DelegateAction.class);
	
	@Autowired
	private IDelegateService delegateService;

	
	@RequestMapping(value = "/delegateDatagrid")
	public String toList_page() throws Exception{
		return "delegate/list_delegate";
	}
	
	
	@RequestMapping("/toList")
	@ResponseBody
	public Datagrid<Object> userList() throws Exception{
		User user = UserUtil.getUserFromSession();		
		List<Delegate> delegateList = delegateService.findByAssignee(user.getId());
		List<Object> jsonList=new ArrayList<Object>(); 
		for (Delegate delegate : delegateList) {
			jsonList.add(BeanUtils.describe(delegate));
		}
		return new Datagrid<Object>(0, jsonList);
	}		
	
	@RequestMapping(value = "/toAdd")
	public String toAdd() throws Exception{
		return "delegate/add_delegate";
	}

	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	@ResponseBody
	public Message doAdd(@ModelAttribute("delegate") Delegate delegate) throws Exception{
		User user = UserUtil.getUserFromSession();
		delegate.setAssignee(user.getId());
		delegate.setEndTime(DateUtil.getEndOfDay(delegate.getEndTime()));
		delegateService.doAdd(delegate);
		return new Message(Boolean.TRUE, "添加成功！");
	}
	
	@RequestMapping(value = "/delete/{id}")
	@ResponseBody
	public Message delete(@PathVariable("id") Integer id) throws Exception{
		if(!com.zml.oa.util.BeanUtils.isBlank(id)){
			delegateService.doDelete(id);		
			return new Message(Boolean.TRUE, "删除成功！");
		}else{
			return new Message(Boolean.FALSE, "删除失败！ID为空！");
		}
	}
    @InitBinder  
    protected void initBinder(HttpServletRequest request,  
                                  ServletRequestDataBinder binder) throws Exception {  
        //对于需要转换为Date类型的属性，使用DateEditor进行处理  
        binder.registerCustomEditor(Date.class, new DateEditor());  
    }  
	
}
