/**
 * 添加请假申请
 */

var workOrder_form;

//初始化表单
$(function(){
	workOrder_form = $('#workOrder_form').form({
	    url: ctx+"/workOrderAction/doAdd",
	    onSubmit: function () {
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
	        	$("#save").linkbutton('disable');
	        	$("#clear").linkbutton('disable');
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
	});
})

//表单提交
function submitForm(){
	workOrder_form.submit();
}

//表单重置
function clearForm(){
	workOrder_form.form('clear');
}

//选择委派人窗口
function chooseUser(){
	//弹出对话窗口
	user_dialog = $('<div/>').dialog({
    	title : "选择任务委派人",
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