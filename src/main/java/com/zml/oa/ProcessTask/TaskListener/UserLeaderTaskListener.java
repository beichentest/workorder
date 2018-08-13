package com.zml.oa.ProcessTask.TaskListener;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zml.oa.entity.User;
import com.zml.oa.entity.UserTask;
import com.zml.oa.service.IUserService;

/**
 * 动态用户任务分配
 * @author ZML
 *
 */
@Component("userLeaderListener")
public class UserLeaderTaskListener implements TaskListener {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4485190091171434132L;

	private static final Logger logger = Logger.getLogger(UserLeaderTaskListener.class);

	@Autowired
	private IUserService userService;
    
	@Override
	public void notify(DelegateTask delegateTask) {		
		try {
			Integer lastId = (Integer) delegateTask.getVariable("lastId");
			User user = userService.loadUserById(lastId);
			if(user!=null) {
				delegateTask.setAssignee(user.getGroup().getLeader().getId().toString());
				logger.info("assignee leader id: "+user.getGroup().getLeader().getId().toString());
				logger.info("给受理人"+user.getGroup().getLeader().getRealName()+"发送邮件通知"+user.getGroup().getLeader().getEmail());
			}else {
				throw new Exception("未查出用户信息");
			}
		} catch (Exception e) {
			logger.error("动态分配上级任务异常", e);
			e.printStackTrace();
		}
	}
}
