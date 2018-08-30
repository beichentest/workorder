package com.zml.oa.ProcessTask.TaskListener;

import java.util.Date;

import org.activiti.engine.EngineServices;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zml.oa.service.IDelegateService;

/**
 * 动态用户任务分配
 * @author ZML
 *
 */
@Component("filterTaskListener")
public class FilterTaskListener implements TaskListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2190559253653576032L;
	
	private static final Logger logger = Logger.getLogger(FilterTaskListener.class);
    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
	private IDelegateService delegateService;
    
	@Override
	public void notify(DelegateTask delegateTask) {
		String originAssginee = delegateTask.getAssignee();
		Integer delegateAssginee = delegateService.getAttorney(Integer.parseInt(originAssginee), new Date());
		if(delegateAssginee!=null&&delegateAssginee>0) {
			delegateTask.setAssignee(delegateAssginee.toString());
			EngineServices engineServices = delegateTask.getExecution().getEngineServices();
			TaskService taskService = engineServices.getTaskService();
			String message ="任务【"+delegateTask.getName()+"】办理人【"+originAssginee+"】自动转给【"+delegateAssginee+"】";
			taskService.addComment(delegateTask.getId(),delegateTask.getProcessInstanceId(),"redirect",message);
		}		
//        String newUser = userMap.get(originAssginee);
//        if(StringUtil.isNotBlank(newUser)){
//            delegateTask.setAssignee(newUser);
//            EngineServices engineServices = delegateTask.getExecution().getEngineServices();
//            TaskService taskService = engineServices.getTaskService();
//            String message =getClass().getName()+"->任务【"+delegateTask.getName()+"】办理人【"+originAssginee+"】自动转给【"+newUser+"】";
//            taskService.saveCommand(delegateTask.getId(),delegateTask.getProcessInstanceId(),"redirect",message);
//            }else{
//
//            }
//        }
	}
}
