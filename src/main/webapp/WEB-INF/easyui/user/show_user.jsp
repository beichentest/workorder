<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
$(function(){
	$("#group").combobox({
		width:160,
		url:"${ctx }/groupAction/getAllGroup",
		valueField: 'id',
		textField: 'name',
		onSelect:function(value){
			$("#group_name").val(value.name);
		},
		required: true,
		onLoadSuccess: function (data) {
			var groupId = $("#groupId").val();
            $("#group").combobox('setValue',groupId);
        }
	});
})
</script>
<style type="text/css">
    #fm{
        margin:0;
        padding:10px 30px;
    }
    .ftitle{
        font-size:14px;
        font-weight:bold;
        padding:5px 0;
        margin-bottom:10px;
        border-bottom:1px solid #ccc;
    }
    .fitem{
        margin-bottom:5px;
    }
    .fitem label{
        display:inline-block;
        width:80px;
    }
    .fitem input{
        width:160px;
    }
</style>

<div id="dlg" class="easyui-layout" style="padding:10px 20px">    
    <form id="user_form" method="post" >
		<input type="hidden" name="id" id="id" />
		<input type="hidden" name="groupId" id="groupId" value="${user.group.id }"/>
		<input type="hidden" name="salt" id="salt" />
		<input type="hidden" name="registerDate" id="registerDate" />
        <div class="fitem">
            <label>用户名:</label>
            <input id="name" name="name" value="${user.name}" class="easyui-textbox easyui-validatebox" required="true" readonly="readonly">
        </div>
        <div class="fitem">
            <label>真实姓名:</label>
            <input id="realName" name="realName" value="${user.realName}" class="easyui-textbox easyui-validatebox" required="true" readonly="readonly">
        </div>
        <div class="fitem">
            <label>密码:</label>
            <input type="password" id="passwd"
                   name="passwd" class="easyui-textbox easyui-validatebox" maxLength="36"
                   data-options="required:true,missingMessage:'请输入密码.',validType:['minLength[1]']">
        </div>
        <div class="fitem">
            <label>确认密码:</label>
            <input type="password" id="repasswd"
                   name="repasswd" class="easyui-textbox easyui-validatebox" required="true"
                   missingMessage="请再次填写密码." validType="equalTo['#passwd']"
                   invalidMessage="两次输入密码不匹配.">
        </div>        
    </form>
</div>

