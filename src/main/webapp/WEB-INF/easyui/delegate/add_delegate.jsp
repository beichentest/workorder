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
        width:150px;
    }
</style>

<div id="dlg" class="easyui-layout" style="padding:10px 20px">
    <div class="ftitle"><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/> 代理人信息</div>
    <form id="delegate_form" method="post" >
        <div class="fitem">
            <label>代理人:</label>
            <input id="userName" name="attorneyName" type="text" readonly="readonly" style="height: 23px;margin-left:0px">
	        <input id="userId" name="attorney" type="hidden"><a class="easyui-linkbutton" icon="icon-search" href="javascript:void(0)" onclick="chooseUser();">选择代理人</a>	                           
        </div>
        <div class="fitem">
            <label>开始时间:</label>
            <input id="startTime" name="startTime" class="easyui-datebox easyui-validatebox" data-options="required:true">
        </div>        
        <div class="fitem">
            <label>结束时间:</label>
            <input id="endTime" name="endTime" class="easyui-datebox easyui-validatebox" data-options="required:true">
        </div>
        <div class="fitem">
            <label>备注:</label>
            <textarea cols="33" rows="3" name="memo"></textarea>
        </div>
    </form>
</div>

