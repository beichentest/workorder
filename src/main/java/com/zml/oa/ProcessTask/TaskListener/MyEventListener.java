package com.zml.oa.ProcessTask.TaskListener;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.zml.oa.entity.User;
import com.zml.oa.service.IUserService;

public class MyEventListener implements ActivitiEventListener {
	private static final Logger logger = Logger.getLogger(MyEventListener.class);
	
	@Autowired
	private IUserService userService;
	
	@Override
	public boolean isFailOnException() {
		logger.error("自定义事件监听出现异常！");
		return false;
	}

	@Override
	public void onEvent(ActivitiEvent event) {
		ActivitiEventType eventType = event.getType();
		
		if (eventType == ActivitiEventType.TASK_CREATED) { // 任务创建，当流程走到一个任务节点时，会进入这个条件
			ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
			logger.info("任务创建时-------------------------------------------------------");
		}
		if (eventType == ActivitiEventType.TASK_ASSIGNED) { // 任务认领，按照谁先登录谁先认领的原则，认领后会给该认领的用户发邮件
			try {
				ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
				TaskEntity entity = (TaskEntity)entityEvent.getEntity();			
				Integer assigneeId = Integer.parseInt(entity.getAssignee());		
				User user = userService.getUserById(assigneeId);
				logger.info("给受理人"+user.getRealName()+"发送邮件通知"+user.getEmail());
			} catch (Exception e) {
				logger.error("受理人发送邮件通知异常", e);
			}
			logger.info("任务分配受理人-------------------------------------------------------");
		}
        if (eventType == ActivitiEventType.TASK_COMPLETED) { //审批完成，首先会进入下一节点
        	logger.info("任务审批完成-------------------------------------------------------");
		}
	}

}
