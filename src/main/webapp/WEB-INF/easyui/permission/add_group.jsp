<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/js/app/permission.js?_=${sysInitTime}"></script>
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
<script type="text/javascript">
$(function() {
	$("#parentId").combotree({
		width:171,
		url:"${ctx }/groupAction/getGroupList",
		idFiled:'id',
	 	textFiled:'name',
	 	parentField:'parentId'	 	
	});
});
</script>
<div class="easyui-layout" style="padding:10px 20px">
    <div class="ftitle"><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/>用户组信息</div>
    <form id="group_form" method="post" >
        <div class="fitem">
            <label>组名称:</label>
            <input id="name" name="name" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>组类型:</label>
            <input id="type" name="type" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>父机构:</label>
            <input name="parentId"  class="easyui-combotree" id="parentId" type="text"/>
        </div>
        <div class="fitem">
            <label>排序:</label>
            <input name="seq"  class="easyui-textbox" id="seq" type="text"/>
        </div>
        <div class="fitem">
            <label>组长:</label>
            <input id="userName" type="text" readonly="readonly" style="height: 23px">
            <!--input id="userName" name="leader" class="easyui-textbox easyui-validatebox" required="true" readonly="readonly"-->
            <input id="userId" name="leader.id" type="hidden"><a class="easyui-linkbutton" icon="icon-search" href="javascript:void(0)" onclick="chooseUser();">选择委派人</a>
        </div>
    </form>
</div>