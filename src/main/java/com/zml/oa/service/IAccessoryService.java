package com.zml.oa.service;

import java.io.Serializable;

import com.zml.oa.entity.Accessory;

public interface IAccessoryService {
	public Serializable doAdd(Accessory accessory) throws Exception;		
	
	public void doDelete(Accessory accessory) throws Exception;
	
	public Accessory findById(Integer id) throws Exception;
}
