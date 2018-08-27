package com.zml.oa.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.zml.oa.entity.User;
import com.zml.oa.service.IUserService;
import com.zml.oa.util.Constants;
/**
 * 
* @ClassName: SynchSessionFilter  
* @Description: 同步session过滤器  
* @author Administrator  
* @date 2018年7月9日  
*
 */
public class SynchSessionFilter extends PathMatchingFilter {

	/**
	 * sAccessAllowed：表示是否允许访问；mappedValue就是[urls]配置中拦截器参数部分，如果允许访问返回true，否则false；	
	 * onAccessDenied：表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可
	 */
	@Autowired
	private IUserService userService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response,
                                  Object mappedValue) throws Exception {
    	Subject currentUser = SecurityUtils.getSubject();
    	User user = (User) currentUser.getSession().getAttribute(Constants.CURRENT_USER);
    	if(user==null&&SecurityUtils.getSubject().getPrincipal()!=null) {
//    		String username = (String) SecurityUtils.getSubject().getPrincipal();
//    		user = this.userService.getUserByName(username);
    		user = (User)SecurityUtils.getSubject().getPrincipal();
    		currentUser.getSession().setAttribute(Constants.CURRENT_USER, user);
        }
        return true;
    }
}
