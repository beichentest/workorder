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
	<script type="text/javascript" src="${ctx}/js/app/delegate.js?_=${sysInitTime}"></script>
  </head>
  <body>
	<div class="well well-small" style="margin-left: 5px;margin-top: 5px">
		<span class="badge">提示</span>
		<p>
			在此你可以指定<span class="label-info"><strong>代理人</strong></span>，目前只支持<span class="label-info"><strong>部门审批</strong></span>，<span class="label-info"><strong>开发</strong></span>任务节点设置代理人!<br/><br/>
		</p>
	</div>
	
	<div id="toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0">
			<tr>
				<td style="padding-left:2px">
					<%-- <shiro:hasRole name="admin"> --%>
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addDelegate();">添加</a>						
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="del();">删除</a>			
					<%-- </shiro:hasRole> --%>					
				 </td>	
			</tr>
		</table>
	</div>
	<table id="delegate_datagrid" title="代理人"></table>
  </body>
</html>
