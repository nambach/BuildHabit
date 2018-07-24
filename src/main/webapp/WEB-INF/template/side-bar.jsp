<%--
  Created by IntelliJ IDEA.
  User: NAMBACH
  Date: 6/27/2018
  Time: 1:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<aside id="left-panel" class="left-panel">
    <nav class="navbar navbar-expand-sm navbar-default">

        <div class="navbar-header">
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#main-menu"
                    aria-controls="main-menu" aria-expanded="false" aria-label="Toggle navigation">
                <i class="fa fa-bars"></i>
            </button>
            <a class="navbar-brand" href="./"><img src="${pageContext.request.contextPath}/resources/template/suffee-admin/images/logo.png" alt="Logo"></a>
            <a class="navbar-brand hidden" href="./"><img src="${pageContext.request.contextPath}/resources/template/suffee-admin/images/logo2.png" alt="Logo"></a>
        </div>

        <div id="main-menu" class="main-menu collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active">
                    <a href="${pageContext.request.contextPath}/">
                        <i class="menu-icon fa fa-dashboard"></i>
                        Dashboard
                    </a>
                </li>

                <h3 class="menu-title">Management</h3><!-- /.menu-title -->
                <li>
                    <a href="${pageContext.request.contextPath}/user-management">
                        <i class="menu-icon fa fa-users"></i>
                        User Manager
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/template-management">
                        <i class="menu-icon fa fa-users"></i>
                        Template Manager
                    </a>
                </li>
                <li>
                    <a href="${context}/tables-data.html">
                        <i class="menu-icon fa fa-users"></i>
                        Table Data
                    </a>
                </li>

                <h3 class="menu-title">Admin tools</h3><!-- /.menu-title -->
                <li>
                    <a href="${pageContext.request.contextPath}/swagger-ui.html">
                        <i class="menu-icon fa fa-tasks"></i>
                        Swagger APIs
                    </a>
                </li>

                <h3 class="menu-title">Account</h3><!-- /.menu-title -->
                <li>
                    <a href="${pageContext.request.contextPath}/">
                        <i class="menu-icon fa fa-sign-out"></i>
                        Logout
                    </a>
                </li>

            </ul>
        </div><!-- /.navbar-collapse -->
    </nav>
</aside><!-- /#left-panel -->

