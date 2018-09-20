package com.zml.oa.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zml.oa.entity.Accessory;
import com.zml.oa.service.IAccessoryService;
import com.zml.oa.service.IBaseService;

@Service
public class IAccessoryServiceImpl implements IAccessoryService {
	@Autowired 
	private IBaseService<Accessory> baseService;
	@Override
	public Serializable doAdd(Accessory accessory) throws Exception {
		return baseService.add(accessory);
	}

	@Override
	public void doDelete(Accessory accessory) throws Exception {
		baseService.delete(accessory);
	}

	@Override
	public Accessory findById(Integer id) throws Exception {
		return baseService.getUnique("Accessory", new String[]{"id"}, new String[]{id.toString()});
	}
}
