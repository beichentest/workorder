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
    <div class="ftitle"><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/> 工单申请确认</div>
    <form id="audit_form" method="post" >
   		<input type="hidden" name="userId" value="${user.id }" />
		<input type="hidden" name="workOrderId" value="${workOrder.id }" />
		<input type="hidden" id="formKey" name="formKey" value="${formKey}" />
		<input type="hidden" id="completeFlag" name="completeFlag" value="" />
		<div class="fitem">
	            <label>类别:</label>
	            <select id="type" class="easyui-combobox" name="type" style="width:280px;" disabled="disabled">	            	
				    <option value="需求变更" <c:if test="${'需求变更'==workOrder.type}">selected='selected'</c:if>>需求变更</option>
				    <option value="新项目" <c:if test="${'新项目'==workOrder.type}">selected='selected'</c:if>>新项目</option>
				    <option value="设计缺陷" <c:if test="${'设计缺陷'==workOrder.type}">selected='selected'</c:if>>设计缺陷</option>
				    <option value="非程序类修改" <c:if test="${'非程序类修改'==workOrder.type}">selected='selected'</c:if>>非程序类修改</option>				    
				</select>	            
	     </div>
		<div class="fitem">
	       <label>项目域名:</label>
	          <select id="domain_id" class="easyui-combobox" name="domain.id" style="width:280px;" disabled="disabled">
				    <c:forEach var="domain" items="${domainList}">
				    <option value="${domain.id}" <c:if test="${domain.id==workOrder.domain.id}">selected='selected'</c:if>>${domain.domain}</option>
				    </c:forEach>
				</select>	            
	        </div>
	        <div class="fitem">
	            <label>涉及项目:</label>
	            <select id="project_id" class="easyui-combobox" name="project.id" style="width:280px;" disabled="disabled">
				    <c:forEach var="project" items="${workOrder.domain.projects}">
				    <option value="${project.id}" <c:if test="${project.id==workOrder.project.id}">selected='selected'</c:if>>${project.name}</option>
				    </c:forEach>
				</select>
	        </div>
	        <div class="fitem">
	            <label>功能需求:</label>
	            <input id="develop_explain" name="developExplain" value="${workOrder.developExplain}" class="easyui-textbox" required="true" readonly="readonly" data-options="multiline:true" style="height:70px; width: 300px">
	        </div>
	         <div class="fitem">
	            <label>测试描述:</label>
	            <input id="test_desc" name="testDesc" value="${workOrder.testDesc}" class="easyui-textbox" required="true" readonly="readonly" data-options="multiline:true" style="height:70px; width: 300px">
	        </div>
	        <div class="fitem">
	            <label>开发人员:</label>	            
	            <input id="userName" name="coder" type="text" value="${workOrder.coder}" readonly="readonly" style="height: 23px;margin-left:0px" readonly="readonly">	            
	        </div>
	        <div class="fitem">
            <label>SVN地址:</label>
	            <input id=coderSvn name="coderSvn" value="${workOrder.coderSvn}" class="easyui-textbox easyui-validatebox" required="true" readonly="readonly">
	        </div>
	        <div class="fitem">
            <label>开发版本:</label>
	            <input id=coderVersion name="coderVersion" value="${workOrder.coderVersion}" class="easyui-textbox easyui-validatebox" required="true" readonly="readonly">
	        </div>
	        <div class="fitem">
            <label>上线版本地址:</label>
	            <input id=testSvn name="testSvn" value="${workOrder.testSvn}"  class="easyui-textbox easyui-validatebox" required="true" readonly="readonly">
	        </div>
	        <div class="fitem">
            <label>上线版本:</label>
	            <input id=testVersion name="testVersion" value="${workOrder.testVersion}"  class="easyui-textbox easyui-validatebox" required="true" readonly="readonly">
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
            <label>回滚原因:</label>
            <input id="rollbackReason" name="rollbackReason" value="${workOrder.rollbackReason}" class="easyui-textbox"  data-options="multiline:true" style="height:70px; width: 300px">            
        </div>        
    </form>
</div>
