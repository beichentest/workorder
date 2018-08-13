<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE html>
<html>
  <head>
    <title>项目管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/app/project.js?_=${sysInitTime}"></script>
  </head>
  <body>
	<div class="well well-small" style="margin-left: 5px;margin-top: 5px">
		<span class="badge">提示</span>
		<p>
			在此你可以对<span class="label-info"><strong>项目</strong></span>进行管理!<br/><br/>
		</p>
	</div>
	
	<div id="toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0">
			<tr>
				<td style="padding-left:2px">
					<%-- <shiro:hasRole name="admin"> --%>
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showUser();">添加</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit();">编辑</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="del();">删除</a>|					
					<%-- </shiro:hasRole> --%>
					<span class="l-btn-text">名称:</span>
					<input id="name" style="line-height:26px;border:1px solid #ccc">					
					<a href="#" class="easyui-linkbutton" class="easyui-linkbutton" plain="true" iconCls="icon-search" onclick="doSearch()">查询</a>
				 </td>	
			</tr>
		</table>
	</div>
	<table id="project_datagrid" title="项目管理"></table>
  </body>
</html>
