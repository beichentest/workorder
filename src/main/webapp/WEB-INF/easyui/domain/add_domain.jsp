<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
/* $(function(){
	$("#group").combobox({
		width:160,
		url:ctx+"/groupAction/getAllGroup",
		valueField: 'id',
		textField: 'name',
		onSelect:function(value){
			$("#group_name").val(value.name);
		},
		required: true,
		onLoadSuccess: function (data) {
            $("#group").combobox('setValue',data[0].id);
        }
	});
}) */
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
        width:110px;
    }
    .fitem input{
        width:250px;
    }
</style>

<div id="dlg" class="easyui-layout" style="padding:10px 20px">
    <div class="ftitle"><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/> 域名信息</div>
    <form id="user_form" method="post" >
        <div class="fitem">
            <label>名称:</label>
            <input id="name" name="name" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>域名:</label>
            <input id="domain" name="domain" class="easyui-textbox easyui-validatebox" required="true">
        </div>                
        <div class="fitem">
            <label>归属:</label>
            <input id="subjection" name="subjection" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>地区:</label>
            <input id="area" name="area" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>负责人:</label>
            <input id="manager" name="manager" class="easyui-textbox easyui-validatebox" required="true">
        </div>        
         <div class="fitem">
            <label>上线日期:</label>
            <input id="releaseDate" name="releaseDate" class="easyui-datebox easyui-validatebox" >
        </div>       
        <div class="fitem">
            <label>备注:</label>
            <textarea cols="33" rows="3" name="memo"></textarea>
        </div>
    </form>
</div>

