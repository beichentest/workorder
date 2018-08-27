package com.zml.oa.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.zml.oa.entity.User;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IUserService;

public class UserServiceImplTest extends BaseJunit4Test{
	@Autowired
	private IUserService userService;
	
	@Test
	public void getUserByName() {
		try {
			User user = userService.getUserByName("lichen");
			System.out.println(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@Transactional   //标明此方法需使用事务  
    @Rollback(false)  //标明使用完此方法后事务不回滚,true时为回滚
	public void doAdd() {
		User user = new User();
		user.setId(-100);
		user.setEmail("tst@sdfd.com");
		user.setName("stest");
		user.setPasswd("123456");
		user.setRealName("中山东南方");
		user.setRegisterDate(new Date());
		try {
			userService.doAdd(user, "1", false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	@Transactional   //标明此方法需使用事务  
    @Rollback(false)  //标明使用完此方法后事务不回滚,true时为回滚
	public void testSaveOrUpdate() {		
		try {
			User user = userService.getUserById(49);
			user.setName("abcdef");
			userService.testSaveOrUpdate(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	@Transactional   //标明此方法需使用事务  
    @Rollback(false)  //标明使用完此方法后事务不回滚,true时为回滚
	public void testDelete() {
		User user;
		try {
			user = userService.getUserById(49);			
			userService.doDelete(user, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	@Transactional   //标明此方法需使用事务  
    @Rollback(false)  //标明使用完此方法后事务不回滚,true时为回滚
	public void testUpdate() {
		User user;
		try {
			user = userService.getUserById(50);
			user.setName("123");
			user.setPasswd("123456");
			userService.doUpdate(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testHqlQuery() {
		try {
			List<User> userList = userService.testHqlQuery("User", new String[] {"group.id","name"}, new String[] {"11","lichen"});
			for (User user : userList) {
				System.out.println(user.getRealName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testGetBean() {
		try {
			User user = userService.testGetBean(User.class, 20);
			System.out.println(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	@Transactional   //标明此方法需使用事务  
	public void testLoadBean() {
		User user;
		try {
			user = userService.testLoadBean(User.class, 20);
			System.out.println(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testGetUserList() {
		Page<User> p = new Page<User>(2, 3);		
		try {
			List<User> userList = userService.getUserList(p);
			for (User user : userList) {
				System.out.println(user);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testGetUserById() {
		try {
			User user = userService.getUserById(20);
			System.out.println(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	public void testGetListPage() {
		String hql = "select u from User u where u.locked=?";
		Page<User> p = new Page<User>(2, 3);
		List<Object> values = new ArrayList<Object>();
		values.add(0);
		try {
			List<User> list =  userService.testGetListPage(hql, p, "id", "desc", values.toArray());
			for (User user : list) {
				System.out.println(user);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testGetUserListLeng() {
		Page<User> p = new Page<User>(3, 5);
		try {
			List<User> userList = userService.getUserList(p, null, null, "id", "asc");
			for (User user : userList) {
				System.out.println(user);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testGet() {
		try {
			List<User> list = userService.testGet("User",  new String[] {"group.id","name"}, new String[] {"11","lichen"});
			for (User user : list) {
				System.out.println(user);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
