<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
        width:110px;
    }
    .fitem input{
        width:250px;
    }
</style>

<div id="dlg" class="easyui-layout" style="padding:10px 20px">
    <div class="ftitle"><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/> 项目信息</div>
    <form id="user_form" method="post" >
    	<input type="hidden" name="id">
    	<input type="hidden" name="status">
        <div class="fitem">
            <label>名称:</label>
            <input id="name" name="name" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>开发语言及版本:</label>
            <input id="language" name="language" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>是否有文档:</label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" id="haveDoc" name="haveDoc" style="width: 20px;" value="0" checked="checked"/> 无
            </label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" id="haveDoc" name="haveDoc" style="width: 20px;" value="1" /> 有
            </label>
        </div>
        <div class="fitem">
            <label>是否有测试用例:</label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" id="haveTest" name="haveTest" style="width: 20px;" value="0" checked="checked"/> 无
            </label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" id="haveTest" name="haveTest" style="width: 20px;" value="1" /> 有
            </label>
        </div>
        <div class="fitem">
            <label>是否有源码:</label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" id="haveCode" name="haveCode" style="width: 20px;" value="0"  checked="checked"/> 无
            </label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" id="haveCode" name="haveCode" style="width: 20px;" value="1" /> 有
            </label>
        </div>
        <div class="fitem">
            <label>是否有发布版本:</label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" id="haveRelease" name="haveRelease" style="width: 20px;" value="0" checked="checked"/> 无
            </label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" id="haveRelease" name="haveRelease" style="width: 20px;" value="1" /> 有
            </label>
        </div>        
        <div class="fitem">
            <label>开发人员:</label>
            <input id="coder" name="coder" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>应用服务器及版本:</label>
            <input id="server" name="server" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>操作系统:</label>
            <input id="os" name="os" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>数据库:</label>
            <input id="db" name="db" class="easyui-textbox easyui-validatebox" required="true">
        </div>
         <div class="fitem">
            <label>发布日期:</label>
            <input id="releaseTime" name="releaseTime" class="easyui-datebox easyui-validatebox" >
        </div>        
        <div class="fitem">
            <label>开发SVN:</label>
            <input id="coderSvn" name="coderSvn" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>上线SVN:</label>
            <input id="testerSvn" name="testerSvn" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>项目归属:</label>
            <input id="home" name="home" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>应用地区:</label>
            <input id="area" name="area" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>项目版本号:</label>
            <input id="versionPrefix" name="versionPrefix" class="easyui-textbox easyui-validatebox" required="true">
        </div>
         <div class="fitem">
            <label>程序起始版本:</label>
            <input id="versionCode" name="versionCode" class="easyui-numberbox easyui-validatebox" required="true" data-options="min:0,value:0">
        </div>
         <div class="fitem">
            <label>非程序起始版本:</label>
            <input id="versionNoCode" name="versionNoCode" class="easyui-numberbox easyui-validatebox" required="true" data-options="min:0,value:0">
        </div>
        <div class="fitem">
            <label>编译说明:</label>
            <textarea cols="33" rows="3" name="buildInfo"></textarea>
        </div>
        <div class="fitem">
            <label>备注:</label>
            <textarea cols="33" rows="3" name="memo"></textarea>
        </div>
    </form>
</div>

