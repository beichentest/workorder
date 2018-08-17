<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE html>
<html>
  <head>
    <title>工单查看</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  <body>
	<div class="well well-small" style="margin-left: 5px;margin-top: 5px">		
		<p>
			
		</p>
	</div>
	
	<div id="toolbar" style="padding:2px 0">		
		<span>发起人:</span>
		<input id="applyUser" style="line-height:26px;border:1px solid #ccc">
		<span>项目名称:</span>
		<input id="projectName" style="line-height:26px;border:1px solid #ccc">
		<span>类别:</span>
		<select id="type" style="width:100px;">
					<option value="" selected='selected'>全部</option>	            	
				    <option value="需求变更" >需求变更</option>
				    <option value="新项目">新项目</option>
				    <option value="设计缺陷">设计缺陷</option>
				    <option value="非程序类修改">非程序类修改</option>				    
		</select>
		<span>入库日期:</span>
		<input id="beginDate" name="beginDate" type="text" class="easyui-datebox" editable="false">——<input id="endDate" name="endDate" type="text" class="easyui-datebox" editable="false">
		<a href="#" class="easyui-linkbutton" class="easyui-linkbutton" plain="true" iconCls="icon-search" onclick="doSearch()">查询</a>
		<a href="#" class="easyui-linkbutton" onclick="return Save_Excel()" iconCls="icon-save" plain="true" title="导出excel文件"></a>
		<%-- <table cellpadding="0" cellspacing="0">
			<tr>
				<td style="padding-left:2px">
					<shiro:hasRole name="admin">
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showUser();">添加</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit();">编辑</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="del();">删除</a>|
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="sync();">同步用户</a>
					</shiro:hasRole>
			</tr>
		</table> --%>
	</div>
	<table id="workOrder_datagrid" title="工单查看" toolbar="#toolbar"></table>
  </body>
  <script type="text/javascript">
  $(function() {
		//数据列表
	    user_datagrid = $('#workOrder_datagrid').datagrid({
	        url: ctx+"/workOrderAction/toList",
	        width : 'auto',
			height :  $(this).height()-85,
			pagination:true,
			rownumbers:true,
			border:false,
			singleSelect:true,
			striped:true,
	        columns : [ 
	            [ 
	              {field : 'id',title : '编号',width : fixWidth(0.05),align : 'left',sortable: true, editor : {type:'validatebox',options:{required:true}}},
	              {field : 'domain.domain',title : '域名',width : fixWidth(0.2),align : 'left',sortable: true, 
	            	  formatter:function(value, row){
	                		return row.domain.domain;
	                	}  
	              },
	              {field : 'domain.name',title : '域名称',width : fixWidth(0.2),align : 'left',sortable: true, 
	            	  formatter:function(value, row){
	                		return row.domain.name;
	                	}  
	              },
	              {field : 'project.name',title : '项目名称',width : fixWidth(0.2),align : 'left',sortable: true, 
	            	  formatter:function(value, row){
	                		return row.project.name;
	                	}  
	              },
	              {field : 'commiterDate',title : '入库时间',width : fixWidth(0.1),align : 'left',sortable: true,
	            	  formatter:function(value,row){
							return moment(value).format("YYYY-MM-DD HH:mm:ss");
						}  
	              },	              	              
	              {field : 'coder',title : '开发人员',width : fixWidth(0.05),align : 'left',sortable: true, editor : {type:'validatebox',options:{required:true}}},
	              {field : 'applyUser',title : '申请人',width : fixWidth(0.05),align : 'left',sortable: true, editor : {type:'validatebox',options:{required:true}}},	              
	              {field : 'tester',title : '测试人',width : fixWidth(0.05),align : 'left',sortable: true, editor : {type:'validatebox',options:{required:true}}},
	              {field : 'commiter',title : '入库人',width : fixWidth(0.05),align : 'left',sortable: true, editor : {type:'validatebox',options:{required:true}}},
	              {field : 'type',title : '类别',width : fixWidth(0.08),align : 'left',sortable: true, editor : {type:'validatebox',options:{required:true}}},
	              {field : 'project.home',title : '项目归属',width : fixWidth(0.05),align : 'left',sortable: true, 
	            	  formatter:function(value, row){
	                		return row.project.home;
	                	}  
	              },
	              {field : 'project.area',title : '应用地区',width : fixWidth(0.05),align : 'left',sortable: true,
	            	  formatter:function(value, row){
	                		return row.project.area;
	                	}	
	              },
	              {field : 'priority',title : '优先级',width : fixWidth(0.05),align : 'left',sortable: true,
	            	  formatter:function(value,row){
	            		  	if(value==50)
	            		  		return "正常";
	            		  	else if(value==60)
	            		  		return "紧急"
	            		  	else
	            		  		return "无"
						}  
	              },
	              {field : 'coderSvn',title : '开发SVN',width : fixWidth(0.2),align : 'left',sortable: true, editor : {type:'validatebox',options:{required:true}}},
	              {field : 'coderVersion',title : '更新版本',width : fixWidth(0.05),align : 'left',sortable: true, editor : {type:'validatebox',options:{required:true}}},
	              {field : 'developExplain',title : '修改内容',width : fixWidth(0.2),align : 'left',
	            	  formatter:function(value,row){
	            		  return row.projectVersion+" "+value;
 					  }
	              },
	              {field : 'rollbackFlag',title : '回滚',width : fixWidth(0.03),align : 'left',
	            	  formatter:function(value,row){
	            		  if(value=="1"){
	            			  return "<font color='red'>是</font>";
	            		  }else{
	            			  return "<font color='green'>否</font>";
	            		  }
 					  }
	              },
	              {field : 'rollbackVersion',title : '回滚版本',width : fixWidth(0.05),align : 'left',editor : {type:'validatebox',options:{required:true}}},
	              {field : 'rollbackReason',title : '回滚原因',width : fixWidth(0.2),align : 'left',editor : {type:'validatebox',options:{required:true}}},
	              {field : 'down',title : '下载',width : fixWidth(0.05),align : 'left', 
	            	  formatter:function(value,row){
	            		  return "<a class='trace' onclick=\"downloadWord('"+row.id+"')\" id='download' href='#'  title='下载'>下载</a>";
 					  }
	              }
	    	    ] 
	        ],
	        toolbar: "#toolbar"
	    });
	    
	    //搜索框
	/*    $("#searchbox").searchbox({ 
	    	menu:"#mm", 
	    	prompt :'模糊查询',
	    	searcher:function(value,name){   
	    		var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
	    		var obj = eval('('+str+')');
	    		$dg.datagrid('reload',obj); 
	    	}
	    
	    });*/
	    
	    //修正宽高
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
  	function downloadWord(id){
  		window.location.href = "downloadWord?id="+id
  	}
  	function doSearch (){
  		$('#workOrder_datagrid').datagrid('load',{  			
  			applyUser: $('#applyUser').val(),
  			projectName: $('#projectName').val(),
  			type: $("#type").val(),
  			beginDate:$("input[name='beginDate']").val(),
  			endDate:$("input[name='endDate']").val()
  		});
  	}
  	function Save_Excel() {//导出Excel文件        
  		var applyUser = $('#applyUser').val();
		var projectName = $('#projectName').val();
		var type = $("#type").val();
		var beginDate = $("input[name='beginDate']").val();
		var endDate = $("input[name='endDate']").val();		
        window.location.href = "saveExcel?applyUser="+applyUser+"&projectName="+projectName+"&type="+type+"&beginDate="+beginDate+"&endDate="+endDate;                
    }
  </script>
</html>
