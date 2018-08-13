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
<title>选取委派人员</title>

</head>
<body>
	<div class="easyui-layout" style="height:300px;margin:0 auto;">        
         <div data-options="region:'north',title:'选择项目',split:true" style="height:100px;display: table;">
             <p style="display: table-cell;vertical-align: middle;text-align: left: ;">
             &nbsp;&nbsp;&nbsp;&nbsp;<input id="project" style="width:200px"></input>
             <a href="javascript:void(0);" class="easyui-linkbutton c1" iconCls="icon-add"  onclick="addProject();">添加项目</a>
             </p>
         </div>         
         <div data-options="region:'center',title:'项目列表'" style="padding:5px;">
         	    <table id="d_project_datagrid" ></table>
         </div>  
      </div>
<script>
var d_project_datagrid;
$(function() {	
    $('#project').combogrid({
    	panelWidth:500,
        delay: 500,
        mode: 'remote',
        url: '${ctx}/projectAction/toComboGridList',
        idField: 'id',
        textField: 'name',
        columns: [[    		
    		{field:'name',title:'名称',width:150,sortable:true},
    		{field:'language',title:'语言',width:80,sortable:true},
    		{field:'coder',title:'开发人员',width:80,sortable:true},
    		{field:'server',title:'服务器',width:80,sortable:true},
    		{field:'releaseTime',title:'发布时间',width:100,sortable:true,
    			formatter : function(value){
                    var date = new Date(value);
                    var y = date.getFullYear();
                    var m = date.getMonth() + 1;
                    var d = date.getDate();
                    return y + '-' +m + '-' + d;
                }	
    		}    		
        ]]
    });
    
    d_project_datagrid = $('#d_project_datagrid').datagrid({
        url: "${ctx}/domainAction/domainProjectList",
        width : 'auto',
		height :  100,
		pagination:false,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'name',title : '名称',width : fixWidth(0.2),align : 'left',sortable: true, editor : {type:'validatebox',options:{required:true}}},
              {field : 'language',title : '语言',width : fixWidth(0.1),align : 'left',sortable: true, editor : {type:'validatebox',options:{required:true}}},
              {field : 'coder',title : '开发人员',width : fixWidth(0.08),align : 'left',editor : {type:'validatebox',options:{required:true}}},
              {field : 'server',title : '服务器',width : fixWidth(0.1),align : 'left',editor : {type:'validatebox',options:{required:true}}},
              /* {field : 'os',title : '操作系统',width : fixWidth(0.08),align : 'left',editor : {type:'validatebox',options:{required:true}}}, */
              /* {field : 'db',title : '数据库',width : fixWidth(0.1),align : 'left',editor : {type:'validatebox',options:{required:true}}}, */
              {field : 'releaseTime',title : '发布时间',width : fixWidth(0.1),editor : "datebox",sortable: true,
            	  formatter : function(value){
                      var date = new Date(value);
                      var y = date.getFullYear();
                      var m = date.getMonth() + 1;
                      var d = date.getDate();
                      return y + '-' +m + '-' + d;
                  }
              }, 
              {field:'opt',title:'操作',width:fixWidth(0.05),
            	  formatter: function(value,row,index){
            		  var btn = '<a class="easyui-linkbutton c1" name="rm_project" iconCls="icon-del" onclick="rmProject(\''+row.id+'\',\'${domainId}\')" href="javascript:void(0)">移除</a>';  
                      return btn;                      
              	  }
              }
    	    ] 
        ],
    	queryParams: {
    		domainId: '${domainId}'    		
    	},
    	onLoadSuccess:function(data){  
    	    $("a[name='rm_project']").linkbutton({text:'移除',plain:true,iconCls:'icon-no'});  
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

function addProject() {
	var grid = $('#project').combogrid('grid');
	var row = grid.datagrid('getSelected');
	if (row) {
		$.ajax({
	        url: ctx + '/domainAction/addProject/',
	        data: {projectId:row.id, domainId:'${domainId}'},
	        type: 'post',
	        dataType: 'json',
	        success: function (data) {
	       	 	if (data.status) {
	       		 	d_project_datagrid.datagrid('load');	// reload the user data
	            }
	            $.messager.show({
					title : data.title,
					msg : data.message,
					timeout : 1000 * 2
				});
	        }
	    });
	}else{
		$.messager.alert("提示", "您未选择任何操作对象，请选择一条数据！");
	}
}

function rmProject(projectId,domainId){
	 $.messager.confirm('确认提示！', '您确定要移除选中项目?', function (result) {
         if (result) {
             $.ajax({
                 url: ctx + '/domainAction/rmProject/',
                 data: {projectId:projectId, domainId:domainId},
                 type: 'post',
                 dataType: 'json',
                 success: function (data) {
                	 if (data.status) {
                		 d_project_datagrid.datagrid('load');	// reload the user data
                     }
                     $.messager.show({
     					title : data.title,
     					msg : data.message,
     					timeout : 1000 * 2
     				});
                 }
             });
         }
     });
}
</script>        		 
</body>
</html>
