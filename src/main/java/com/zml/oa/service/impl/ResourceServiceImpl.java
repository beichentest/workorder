package com.zml.oa.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.zml.oa.dao.IJdbcDao;
import com.zml.oa.entity.GroupAndResource;
import com.zml.oa.entity.Resource;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IBaseService;
import com.zml.oa.service.IResourceService;
import com.zml.oa.util.StringUtils;

@Service
public class ResourceServiceImpl implements IResourceService {

	@Autowired 
	private IBaseService<Resource> baseService;
	
	@Autowired
	protected IJdbcDao jdbcDao;
	
	@Override
	public Resource getPermissions(Integer id) throws Exception {
		Resource res = this.baseService.getUnique("Resource", new String[]{"id", "available"}, new String[]{id.toString(), "1"});
		return res;
	}

	@Override
	public List<Resource> getMenus(List<GroupAndResource> gr) throws Exception {
		List<Integer> resourceIdList = new ArrayList<Integer>();
		for(GroupAndResource gar : gr){
			resourceIdList.add(gar.getResourceId());
		}
		String sql = "select * from t_resource where type=? and parent_id>0 and id in ("+StringUtils.convertListToString(resourceIdList)+")";
		List params = new ArrayList();
		params.add("menu");		
		List<Resource> menus =  baseService.findBySQL(Resource.class, sql, params);
//		List<Resource> menus = new ArrayList<Resource>();
//		for(GroupAndResource gar : gr){
//			Resource resource= getPermissions(gar.getResourceId());
//			if(!BeanUtils.isBlank(resource)){
//				if(resource.isRootNode()) {
//	                continue;
//	            }
//	            if(!"menu".equals(resource.getType())) {
//	                continue;
//	            }
//	            menus.add(resource);
//			}
//		}
		return menus;
	}

	@Override
	public List<Resource> getAllResource() throws Exception {
		return this.baseService.getAllList("Resource");
	}

	@Override
	public List<Resource> getResourceListPage() throws Exception {
		return this.baseService.findByPage("Resource", new String[]{"available"}, new String[]{"1"});
	}

	@Override
	public List<Resource> getResourceByType() throws Exception {
		return this.baseService.findByWhere("Resource", new String[]{"type"}, new String[]{"menu"});
	}

	@Override
	public void doAdd(Resource entity) throws Exception {
		this.baseService.add(entity);
		
	}

	@Override
	public void doUpdate(Resource entity) throws Exception {
		this.baseService.update(entity);
		
	}

	@Override
	public void doDelete(Resource entity) throws Exception {
		this.baseService.delete(entity);
		
	}

	@Override
	public List<Resource> getResourceList(Page<Resource> p) throws Exception {
		return this.baseService.getListPage("Resource", new String[]{"available"}, new String[]{"1"}, p);
	}

	@Override
	public void doDelete(String id) throws Exception {
		String sql = "delete from T_RESOURCE where id=:id ";
		Map<String, Object> paramMap = new HashMap<String, Object>();  
	    paramMap.put("id", id);  
		this.jdbcDao.delete(sql, paramMap);
		
	}

}
