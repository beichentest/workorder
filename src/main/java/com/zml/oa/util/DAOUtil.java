package com.zml.oa.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DAOUtil {
	public static List queryMapsToList(Class entiClass, List dataList) throws InstantiationException, IllegalAccessException {
		List list = new ArrayList();
		// 定义指定bean实例对象
		Object obj = null;
		Map dataMap = null;
		for (int i = 0; i < dataList.size(); i++) {
			obj = entiClass.newInstance();
			dataMap=(Map) dataList.get(i);
			Set<String> keySet = dataMap.keySet();
			for (String string : keySet) {
				// 获取列名
				String colName = string.toLowerCase();
				// 设置方法名
				String methodName = "set" + StringUtils.returnPOColName(colName);
				// 设置属性名
				String filedstr = StringUtils.returnPOColName(colName);
				filedstr = filedstr.replaceFirst(filedstr.charAt(0) + "",
						new String(filedstr.charAt(0) + "").toLowerCase());
				// 获取当前位置的值，返回Object类型
				Object value = dataMap.get(string);
				// 利用反射机制，生成setXX()方法的Method对象并执行该setXX()方法。
				try {
					Method method = null;
					if (value != null) {
						if(value instanceof java.lang.Integer){
							method = obj.getClass().getMethod(methodName,
									java.lang.Integer.class);
							method.invoke(obj, (java.lang.Integer) value);
						}else if (value instanceof java.lang.Number) {
							Field aa = entiClass.getDeclaredField(filedstr);
							Object reValue = (Object) ObjectUtils.convert(
									value, aa.getType());
							method = obj.getClass().getMethod(methodName,
									aa.getType());
							method.invoke(obj, reValue);
						}else if (value instanceof java.util.Date) {
							method = obj.getClass().getMethod(methodName,
									java.util.Date.class);
							method.invoke(obj, (java.util.Date) value);
						}else if (value instanceof java.sql.Date){
							method = obj.getClass().getMethod(methodName,
									java.sql.Date.class);
							method.invoke(obj, (java.sql.Date) value);
						} else {
							method = obj.getClass().getMethod(methodName,
									value.getClass());
							method.invoke(obj, value);
						}
					}
				} catch (Exception e) {

				}
			}
			list.add(obj);
		}
		return list;
	}
}
