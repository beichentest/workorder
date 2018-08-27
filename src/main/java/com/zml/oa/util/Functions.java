package com.zml.oa.util;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.util.CollectionUtils;

import com.zml.oa.entity.User;

/**
 * 自定义标签
 * @author zml
 *
 */
public class Functions {


    public static boolean in(Iterable iterable, Object element) {
        if(iterable == null) {
            return false;
        }
        return CollectionUtils.contains(iterable.iterator(), element);
    }

    public static String principal(Session session) {
        PrincipalCollection principalCollection =
                (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        User user = (User)principalCollection.getPrimaryPrincipal();
        return user.getName();
    }
    public static boolean isForceLogout(Session session) {
        return session.getAttribute(Constants.SESSION_FORCE_LOGOUT_KEY) != null;
    }
}

