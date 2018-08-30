/**
 * 用户页面相关
 */

var user_datagrid;
var delegate_form;
var user_dialog;
var delegate_dialog;


$(function() {
	//数据列表
    user_datagrid = $('#delegate_datagrid').datagrid({
        url: ctx+"/delegateAction/toList",
        width : 'auto',
		height :  $(this).height()-85,
		pagination:false,
		rownumbers:false,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'attorneyName',title : '代理人',width : fixWidth(0.2),align : 'left',editor : {type:'validatebox',options:{required:true}}},              
              {field : 'startTime',title : '开始时间',width : fixWidth(0.2),editor : "datebox",sortable: true,
            	  formatter : function(value){
                      var date = new Date(value);
                      var y = date.getFullYear();
                      var m = date.getMonth() + 1;
                      var d = date.getDate();
                      return y + '-' +m + '-' + d;
                  }
              },
              {field : 'endTime',title : '结束时间',width : fixWidth(0.2),editor : "datebox",sortable: true,
            	  formatter : function(value){
                      var date = new Date(value);
                      var y = date.getFullYear();
                      var m = date.getMonth() + 1;
                      var d = date.getDate();
                      return y + '-' +m + '-' + d;
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

//初始化表单
function formInit() {
	var _url = ctx+"/delegateAction/doAdd";	
	delegate_form = $('#delegate_form').form({
        url: _url,
        onSubmit: function (param) {
            $.messager.progress({
                title: '提示信息！',
                text: '数据处理中，请稍后....'
            });
            var startTime = $("input[name='startTime']").val();;
            var endTime = $("input[name='endTime']").val();;
            var attorney = $("input[name='attorney']").val();;
            var isValid = true;
            if(attorney==''){
            	$.messager.progress('close');
            	$.messager.alert("错误提示", "代理人不能为空！","error");
            	isValid = false;
            }
            else if(startTime==''){
            	$.messager.progress('close');
            	$.messager.alert("错误提示", "开始时间不能为空！","error");
            	isValid = false;
            }
            else if(endTime==''){
            	$.messager.progress('close');
            	$.messager.alert("错误提示", "结束时间不能为空！","error");
            	isValid = false;
            }            
            return isValid;
        },
        success: function (data) {
            $.messager.progress('close');
            var json = $.parseJSON(data);
            if (json.status) {
            	delegate_dialog.dialog('destroy');//销毁对话框
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
function addDelegate() {
	var _url = ctx+"/delegateAction/toAdd";	
    //弹出对话窗口
	delegate_dialog = $('<div/>').dialog({
    	title : "设置代理人",
		top: 20,
		width : 500,
		height : 380,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
        	formInit();
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	delegate_form.submit();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	delegate_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	delegate_dialog.dialog('destroy');
        }
    });
}

//删除用户
function del() {
    var row = user_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中的所有行?', function (result) {
            if (result) {
                $.ajax({
                    url: ctx + '/delegateAction/delete/'+row.id,
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
//选择委派人窗口
function chooseUser(){
	//弹出对话窗口
	user_dialog = $('<div/>').dialog({
    	title : "选择代理人",
		top: 20,
		width : 1000,
		height : 400,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/userAction/toChooseDelegateUser",
        onClose: function () {
        	user_dialog.dialog('destroy');
        }
    });
}
function addTab(title, groupId){
	if ($('#userTabs').tabs('exists', title)){
		$('#userTabs').tabs('select', title);
	} else {
		var url = ctx+"/userAction/toShowDelegateUser?groupId="+groupId;
		var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
		$('#userTabs').tabs('add',{
			title:title,
			content:content,
			closable:true
		});
	}
}

//取消选择--delegate_user.jsp
function destroy_chooseUser(){
	$("#userName").val("");
	$("#userId").val("");
	user_dialog.dialog('destroy');
}

//选择人时，同时也对父页面赋值了。所以，确认键就只是关闭页面--delegate_user.jsp
function set_chooseUser(){
	user_dialog.dialog('destroy');
}

function convert(rows){
	function exists(rows, parentId){
		for(var i=0; i<rows.length; i++){
			if (rows[i].id == parentId) return true;
		}
		return false;
	}
	
	var nodes = [];
	// get the top level nodes
	for(var i=0; i<rows.length; i++){
		var row = rows[i];
		if (!exists(rows, row.parentId)){
			nodes.push({
				id:row.id,
				text:row.name
			});
		}
	}
	
	var toDo = [];
	for(var i=0; i<nodes.length; i++){
		toDo.push(nodes[i]);
	}
	while(toDo.length){
		var node = toDo.shift();	// the parent node
		// get the children nodes
		for(var i=0; i<rows.length; i++){
			var row = rows[i];
			if (row.parentId == node.id){
				var child = {id:row.id,text:row.name,attributes:{url:row.url}};
				if (node.children){
					node.children.push(child);
				} else {
					node.children = [child];
				}
				toDo.push(child);
			}
		}
	}
	return nodes;
}