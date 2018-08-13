<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html>
<html>
  <head>
    <title>任务管理</title>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
	<meta http-equiv="description" content="This is my page"/>
	<link href="${ctx }/css/common/jquery.qtip.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctx}/js/app/task.js?_=${sysInitTime}" type="text/javascript"></script>
	<script src="${ctx}/js/app/jump.js?_=${sysInitTime}" type="text/javascript"></script>
	<script src="${ctx }/js/trace.js" type="text/javascript"></script>
	<script src="${ctx }/js/common/jquery.outerhtml.js" type="text/javascript"></script>
	<script src="${ctx }/js/common/jquery.qtip.min.js" type="text/javascript"></script>
  </head>
  <body>
	 <div class="well well-small" style="margin-left: 5px;margin-top: 5px;">
		<span class="badge easyui-tooltip" title="提示">提示</span>
		<p>
			在此你可以在<span class="label-info"><strong>待办任务</strong></span>中办理待处理的任务，也可以查看<span class="label-info"><strong>已完成的任务</strong></span>列表。<br/>			
		</p>
	 </div>	
	 <div id="toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0">
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-ok" onclick="handleTask();">办理</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-ok" onclick="claimTask();">签收</a>					
				</td>
			</tr>
		</table>
	 </div>
	 <div id="tabs" class="easyui-tabs">
		<div title="待办任务" closable="true" data-options="selected:true" style="padding:5 0 0 0;">
			<table id="todoTask" title="待办任务列表"></table>
		</div>
		<div title="已完成的任务" closable="true" style="padding:5 0 0 0;">
			<table id="endTask" title="已完成任务"></table>
		</div>
	</div>
	
	<div id="task" class="easyui-dialog" closed="true">
		<form id="taskForm" style="margin: 10px 10px" method="post">
			
		</form>
	</div>
	
	<div id="jumpTask" class="easyui-dialog" closed="true">
		<form id="jumpForm" style="margin: 10px 10px" method="post">
			当前任务节点：<span class="label-info"><strong><label id="currentTaskName"></label></strong></span><br/>
			选择要跳转的节点：<br/>
			<input id=targetTaskDefinitionKey name="targetTaskDefinitionKey" >
		</form>
	</div>
  </body>
</html>
