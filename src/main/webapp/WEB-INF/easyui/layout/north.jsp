<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" charset="utf-8">
	function logout(b) {
		$.messager.confirm("提示", "确认退出吗?",function(r){
			if(r){
				window.location.href="${ctx}/logout";
			}
		});
		
	}

	var user_dialog;
	var user_form;
	function showUserInfo() {
		user_dialog = $('<div/>').dialog({
			modal : true,
			title : '当前用户信息',
			width : 350,
			height : 250,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			href : '${ctx}/userAction/showUser',
			onLoad: function () {
	            formInit();	           
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
			onClose : function() {
				user_dialog.dialog('destroy');
			}
		});
	}
	function formInit() {
	    var _url = ctx+"/userAction/doUpdateUser";		
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
</script>
<div style="position: absolute; right: 0px; bottom: 0px; ">
	<shiro:principal/>&nbsp;&nbsp;你好，欢迎登录！&nbsp;&nbsp;
	<a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_kzmbMenu" iconCls="icon-help">控制面板</a> 
	<a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_zxMenu" iconCls="icon-logout">注销</a>
</div>
<div id="layout_north_kzmbMenu" style="width: 100px; display: none;">
	<div onclick="showUserInfo();">个人信息</div>
	<div class="menu-sep"></div>
	<div>
		<span>更换主题</span>
		<div style="width: 120px;">
			<div onclick="changeTheme('default');">default</div>
			<div onclick="changeTheme('gray');">gray</div>
			<div onclick="changeTheme('cupertino');">cupertino</div>
			<div onclick="changeTheme('dark-hive');">dark-hive</div>
			<div onclick="changeTheme('pepper-grinder');">pepper-grinder</div>
			<div onclick="changeTheme('sunny');">sunny</div>
		</div>
	</div>
</div>
<div id="layout_north_zxMenu" style="width: 100px; display: none;">
	<div onclick="loginAndRegDialog.dialog('open');">锁定窗口</div>
	<div class="menu-sep"></div>
	<div onclick="logout();">重新登录</div>
	<div onclick="logout(true);">退出系统</div>
</div>
