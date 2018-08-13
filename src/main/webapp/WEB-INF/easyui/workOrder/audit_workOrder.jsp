<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
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
    <div class="ftitle"><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/> 工单申请审批</div>
    <form id="audit_form" method="post" >
   		<input type="hidden" name="userId" value="${user.id }" />
		<input type="hidden" name="workOrderId" value="${workOrder.id }" />
		<input type="hidden" id="formKey" name="formKey" value="${formKey}" />
		<input type="hidden" id="completeFlag" name="completeFlag" value="" />
		<div class="fitem">
	       <label>涉及项目:</label>
	          <select id="project_id" class="easyui-combobox" name="projectId" disabled="disabled" style="width:160px;">
				    <option value="0">SIP科教</option>
				    <option value="1">SIP北京乡医</option>
				    <option value="2">SIP——ICME</option>
				</select>	            
	        </div>
	        <div class="fitem">
	            <label>项目域名:</label>
	            <input id="domain" name="domain" value="${workOrder.domain}" class="easyui-textbox easyui-validatebox" readonly="readonly" required="true">
	        </div>
	        <div class="fitem">
	            <label>功能需求:</label>
	            <input id="develop_explain" name="developExplain" value="${workOrder.developExplain}" class="easyui-textbox" required="true" readonly="readonly" data-options="multiline:true" style="height:70px; width: 300px">
	        </div>
	        <div class="fitem">
	            <label>开发人员:</label>	            
	            <input id="userName" name="coder" type="text" value="${workOrder.coder}" readonly="readonly" style="height: 23px;margin-left:0px" readonly="readonly">
	            <input id="userId" name="coderId" type="hidden" value="${workOrder.coderId}"><a class="easyui-linkbutton" icon="icon-search" href="javascript:void(0)" onclick="chooseUser();">选择委派人</a>
	        </div>
        <div class="fitem">
            <label>评论:</label>
			<c:choose>
         		<c:when test="${empty commentList }">
         			暂无评论！
           		</c:when>
            	<c:otherwise>
            		<div style="display: inline-block;">
            		<table class="easyui-datagrid" style="width:450px;" data-options="fitColumns:true,singleSelect:true">
					    <thead>
							<tr>
								<th data-options="field:'userName',width:100,align:'center'">评论人</th>
								<th data-options="field:'time',width:100,align:'center'">评论时间</th>
								<th data-options="field:'content',width:200,align:'center'">评论内容</th>
							</tr>
					    </thead>
					    <tbody>
					    	<c:forEach var="comment" items="${commentList}">
								<tr>
									<td>${comment.userName}</td>
									<td><fmt:formatDate value="${comment.time }" type="date" /></td>
									<td>${comment.content}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					</div>
            	</c:otherwise>
            </c:choose>
        </div>
        <div class="fitem">
            <label>我的意见:</label>
            <textarea cols="33" rows="5" name="content"></textarea>
        </div>
    </form>
</div>
