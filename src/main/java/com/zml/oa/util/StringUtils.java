package com.zml.oa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	/**
     * 下划线转驼峰法
     * @param line 源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line,boolean smallCamel){
        if(line==null||"".equals(line)){
            return "";
        }
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(smallCamel&&matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
            int index=word.lastIndexOf('_');
            if(index>0){
                sb.append(word.substring(1, index).toLowerCase());
            }else{
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }
    /**
     * 驼峰法转下划线
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line){
        if(line==null||"".equals(line)){
            return "";
        }
        line=String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end()==line.length()?"":"_");
        }
        return sb.toString();
    }
}
