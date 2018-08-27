package com.zml.oa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * 
 * @ClassName: StringUtils
 * @Description:TODO(String工具类)
 * @author: zml
 * @date: 2014-4-18 上午10:19:58
 *
 */
public class StringUtils {
	public static boolean isBlank(String... judgeString) {

		for (String str : judgeString) {
			if (str == null || "".equals(str)) {
				return true;
			}
		}
		return false;
	}

	public static String returnPOColName(String dbColName) {

		String[] strs = dbColName.split("_");
		String poColName = "";
		for (int i = 0; i < strs.length; i++) {
			poColName = poColName
					+ (strs[i].replaceFirst(strs[i].charAt(0) + "", new String(strs[i].charAt(0) + "").toUpperCase()));
		}
		return poColName;
	}

	public static boolean isNull(Object object) {
		if (object == null)
			return true;
		if (object instanceof java.lang.String) {
			if ("".equals(object.toString().trim())) {
				return true;
			}
		}
		return false;
	}

	public static Date getNextDay(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	public static String getyyyyMMdd() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	public static Date getDate(String dastr) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(dastr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isEmpty(String str) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(str))
			return true;
		if ("".equals(str.trim()))
			return true;
		return false;
	}

	public static String checkSql(String str) {
		str = str.replace("&", "&amp;");
		str = str.replace("<", "&lt;");
		str = str.replace(">", "&gt");
		str = str.replace("'", "''");
		// str = str.replace("*", "");
		str = str.replace("\n", "<br/>");
		str = str.replace("\r\n", "<br/>");
		str = str.replace("select", "");
		str = str.replace("insert", "");
		str = str.replace("update", "");
		str = str.replace("delete", "");
		str = str.replace("create", "");
		str = str.replace("drop", "");
		str = str.replace("delcare", "");
		// str = str.replace(" ", "&nbsp;");
		str = str.trim();
		return str;
	}
	
	public static String convertListToString(List strlist){
		StringBuffer sb = new StringBuffer();
		if(CollectionUtils.isNotEmpty(strlist)){
			for (int i=0;i<strlist.size();i++) {
				if(i==0){
					sb.append("'").append(strlist.get(i)).append("'");
				}else{
					sb.append(",").append("'").append(strlist.get(i)).append("'");
				}
			}
		}
		return sb.toString();
	}
}
