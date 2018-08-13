/**
 * 用户页面相关
 */

var user_datagrid;
var user_form;
var user_dialog;


$(function() {
	//数据列表
    user_datagrid = $('#project_datagrid').datagrid({
        url: ctx+"/projectAction/toList",
        width : 'auto',
		height :  $(this).height()-85,
		pagination:true,
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
              {field : 'os',title : '操作系统',width : fixWidth(0.08),align : 'left',editor : {type:'validatebox',options:{required:true}}},
              {field : 'db',title : '数据库',width : fixWidth(0.1),align : 'left',editor : {type:'validatebox',options:{required:true}}},
              {field : 'releaseTime',title : '发布时间',width : fixWidth(0.1),editor : "datebox",sortable: true,
            	  formatter : function(value){
                      var date = new Date(value);
                      var y = date.getFullYear();
                      var m = date.getMonth() + 1;
                      var d = date.getDate();
                      return y + '-' +m + '-' + d;
                  }
              }/*,
              {field : 'discardTime',title : '下线时间',width : fixWidth(0.1),editor : "datebox",sortable: true,
            	  formatter : function(value){
                      var date = new Date(value);
                      var y = date.getFullYear();
                      var m = date.getMonth() + 1;
                      var d = date.getDate();
                      return y + '-' +m + '-' + d;
                  }  
              },
              {field : 'status',title : '状态',width : fixWidth(0.05),sortable: true,
            	  formatter:function(value,row){
	        		  if("0"==row.status){
							return "<font color=green>正常<font>";
	          		  }else{
	          			return "<font color=red>下线<font>";  
	          		  }
				  },
          	  editor : "text"} */             
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

//初始化表单
function formInit(row) {
	var _url = ctx+"/projectAction/doAdd";
	if (row != undefined && row.id) {
		_url = ctx+"/projectAction/doUpdate";
	}
    user_form = $('#user_form').form({
        url: _url,
        onSubmit: function (param) {
            $.messager.progress({
                title: '提示信息！',
                text: '数据处理中，请稍后....'
            });
            var isValid = $(this).form('validate');
            if (!isValid) {
                $.messager.progress('close');
            }
            return isValid;
        },
        success: function (data) {
            $.messager.progress('close');
            var json = $.parseJSON(data);
            if (json.status) {
                user_dialog.dialog('destroy');//销毁对话框
                user_datagrid.datagrid('reload');//重新加载列表数据
            } 
            $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
        }
    });
}


//显示弹出窗口 新增：row为空 编辑:row有值
function showUser(row) {
	var _url = ctx+"/projectAction/toAdd";
	if (row != undefined && row.id) {
		_url = ctx+"/projectAction/toUpdate/"+row.id;
	}
    //弹出对话窗口
    user_dialog = $('<div/>').dialog({
    	title : "项目信息",
		top: 20,
		width : 500,
		height : 700,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
            formInit(row);
            if (row) {
            	user_form.form('load', row);
            } else {
            	$("input[name=locked]:eq(0)").attr("checked", 'checked');//状态 初始化值
            }

        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {                	
                    user_form.submit();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    user_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            user_dialog.dialog('destroy');
        }
    });
}



//编辑
function edit() {
    //选中的行（第一次选择的行）
    var row = user_datagrid.datagrid('getSelected');
    if (row) {
        showUser(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除用户
function del() {
    var row = user_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的所有行?', function (result) {
            if (result) {
                $.ajax({
                    url: ctx + '/projectAction/delete/'+row.id,
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.status) {
                            user_datagrid.datagrid('load');	// reload the user data
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
    } else {
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

function doSearch (){
		$('#project_datagrid').datagrid('load',{
			name: $('#name').val()			
		});
	}
