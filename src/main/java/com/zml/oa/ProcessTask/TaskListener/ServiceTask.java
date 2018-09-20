package com.zml.oa.ProcessTask.TaskListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.deepoove.poi.XWPFTemplate;
import com.zml.oa.entity.User;
import com.zml.oa.entity.WorkOrder;
import com.zml.oa.service.IUserService;
import com.zml.oa.service.IWorkOrderService;
import com.zml.oa.util.DateUtil;
/**
 * 
* @ClassName: ServiceTask  
* @Description: 工作流自动执行任务  
* @author lichen  
* @date 2018年9月14日  
*
 */
@Service
public class ServiceTask {
	private static final Logger logger = Logger.getLogger(ServiceTask.class);
    @Resource(name="mailSender")
	private JavaMailSenderImpl mailSender; 
	@Autowired
	private IWorkOrderService workOrderService;
	@Autowired
	private IUserService userService;
	
	@Async
	public void sendMail(DelegateExecution execution) {
		String webPath = (String)execution.getVariable("webPath");
		WorkOrder baseWorkOrder = (WorkOrder) execution.getVariable("entity");
		if(baseWorkOrder!=null&&baseWorkOrder.getDomain()!=null) {
			if("1".equals(baseWorkOrder.getDomain().getMailFlag())) {
		        try {		        	
		        	XWPFTemplate template = workOrderService.generatePrintWorkorder(webPath, baseWorkOrder);
		        	ByteArrayOutputStream ostream = new ByteArrayOutputStream();
					template.write(ostream);
					ByteArrayInputStream swapStream = new ByteArrayInputStream(ostream.toByteArray());
					User user = userService.getUserById(baseWorkOrder.getApplyUserId());
					MimeMessage mimeMessage = mailSender.createMimeMessage();
					MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
			    	messageHelper.setSubject(DateUtil.getTodayString()+" 上线工单");
			    	messageHelper.setFrom(mailSender.getUsername(), "好医生");    		    	
			    	messageHelper.setTo(user.getEmail());
			    	messageHelper.setText(baseWorkOrder.getTitle());
			    	messageHelper.addAttachment("workorder.docx", new ByteArrayResource(IOUtils.toByteArray(swapStream)));
					mailSender.send(mimeMessage);
					logger.info("send mail :"+user.getEmail()+" workorderId="+baseWorkOrder.getId());			
				} catch (Exception e) {
					logger.error(e);
				}
	        }
        }else {
        	logger.error("==========send fail============="+execution.toString());
        }
	}
}
