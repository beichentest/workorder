<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
  <head>
    <title>工单管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/app/workOrder.js?_=${sysInitTime}"></script>
	<style type="text/css">
	    #fm{
	        margin:0;
	        padding:10px 30px;
	    }
	    .ftitle{
	        font-size:14px;
	        font-weight:bold;
	        padding:5px 0;
	        margin-bottom:10px;
	        border-bottom:1px solid #ccc;
	    }
	    .fitem{
	        margin-bottom:5px;
	    }
	    .fitem label{
	        display:inline-block;
	        width:80px;
	        text-align: right;
	    }
	    .fitem input{
	        width:160px;
	        margin-left: 150px;
	    }
	</style>
  </head>
  <body>
  	<div id="dlg" class="easyui-layout" style="padding:10px 20px">
	    <div class="ftitle"><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/> 工单信息</div>
	    <form id="workOrder_form" method="post">
	    	<div class="fitem">
	            <label>优先级:</label>	 
	            <span style="text-align: left;width: 60px;">                       	
                <input type="radio" id="priority" name="priority" style="width: 15px;margin-left: 10px;" value="50" checked="checked"/>正常                            	
               	<input type="radio" id="priority" name="priority" style="width: 15px;margin-left: 10px;" value="60" /> 紧急
               	</span>
	        </div>
	        <div class="fitem">
	            <label>类别:</label>
	            <select id="type" class="easyui-combobox" name="type" style="width:280px;">	            	
				    <option value="需求变更" selected='selected'>需求变更</option>
				    <option value="新项目">新项目</option>
				    <option value="设计缺陷">设计缺陷</option>
				    <option value="非程序类修改">非程序类修改</option>				    
				</select>	            
	        </div>
	        <div class="fitem">
	            <label>项目域名:</label>
	            <select id="domain_id" class="easyui-combobox" name="domain.id" style="width:280px;">
	            	<c:forEach var="domain" items="${domainList}">
				    <option value="${domain.id}">${domain.domain}</option>
				    </c:forEach>
				</select>	            
	        </div>
	        <!-- <div class="fitem">
	            <label>项目域名:</label>
	            <input id="domain" name="domain" class="easyui-textbox easyui-validatebox" required="true">
	        </div> -->
	        <div class="fitem">
	            <label>功能需求:</label>
	            <input id="develop_explain" name="developExplain" class="easyui-textbox" required="true" data-options="multiline:true" style="height:70px; width: 300px">
	        </div>
	        <div class="fitem">
	            <label>开发人员:</label>	            
	            <input id="userName" name="coder" type="text" readonly="readonly" style="height: 23px;margin-left:0px" readonly="readonly">
	            <input id="userId" name="coderId" type="hidden"><a class="easyui-linkbutton" icon="icon-search" href="javascript:void(0)" onclick="chooseUser();">选择委派人</a>
	        </div>
	        <div style="padding:20px 0px 0px 0px;">
				<a href="#" id="save" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="submitForm()" style="width:80px">申请</a>
				<a href="#" id="clear" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" onclick="clearForm()" style="width:80px">重置</a>
			</div>
	    </form>
	</div>
  </body>
</html>