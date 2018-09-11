<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<%-- <script type="text/javascript" src="${ctx}/js/app/delegate_user.js?_=${sysInitTime}"></script>    --%> 
<title>域名信息</title>

</head>
<body>
	<div class="easyui-layout" style="height:300px;margin:0 auto;">                         
         <div data-options="region:'center',title:'域名列表'" style="padding:5px;">
         	    <table id="d_project_datagrid" ></table>
         </div>  
      </div>
<script>
var d_project_datagrid;
$(function() {	
    d_project_datagrid = $('#d_project_datagrid').datagrid({
        url: "${ctx}/projectAction/showDomain",
        width : 'auto',
		height :  100,
		pagination:false,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'id',title : 'ID',width : fixWidth(0.05),align : 'left',sortable: true, editor : {type:'validatebox',options:{required:true}}},	
              {field : 'name',title : '名称',width : fixWidth(0.2),align : 'left',sortable: true, editor : {type:'validatebox',options:{required:true}}},
              {field : 'domain',title : '域名',width : fixWidth(0.2),align : 'left',sortable: true, editor : {type:'validatebox',options:{required:true}}},
              {field : 'manager',title : '负责人',width : fixWidth(0.08),align : 'left',editor : {type:'validatebox',options:{required:true}}},
              {field : 'motorRoom',title : '机房',width : fixWidth(0.1),align : 'left',editor : {type:'validatebox',options:{required:true}}}              
    	    ] 
        ],
    	queryParams: {
    		projectId: '${projectId}'    		
    	}
    }); 
    
    function fixHeight(percent)   
	{   
		return parseInt($(this).width() * percent);
		//return (document.body.clientHeight) * percent ;    
	}

	function fixWidth(percent)   
	{   
		return parseInt(($(this).width() - 30) * percent);
		//return (document.body.clientWidth - 50) * percent ;    
	}
});

</script>        		 
</body>
</html>
