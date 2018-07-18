<%--
  Created by IntelliJ IDEA.
  User: NAMBACH
  Date: 7/18/2018
  Time: 2:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Everyday Awesome - Login</title>
    <meta name="description" content="Everyday Awesome Portal - Administration Tools">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <%@include file="../template/resource-stylesheet.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/app.css">
</head>
<body class="bg-dark">


<div class="sufee-login d-flex align-content-center flex-wrap">
    <div class="container">
        <div class="login-content">
            <div class="login-logo">
                <a href="${pageContext.request.contextPath}/">
                    <img class="align-content"
                         src="${pageContext.request.contextPath}/resources/template/suffee-admin/images/logo.png"
                         alt="">
                </a>
            </div>
            <div class="login-form">
                    <div class="form-group">
                        <label>Username Account</label>
                        <input type="text" id="username" class="form-control" placeholder="Username">
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" id="password" class="form-control" placeholder="Password">
                    </div>
                    <div class="form-group hidden" style="color: #ff0816;" align="center" id="message">
                        Invalid username or password!
                    </div>
                    <button id="btnSubmit" class="btn btn-success btn-flat m-b-30 m-t-30">Sign in</button>

            </div>
        </div>
    </div>
</div>
</body>

<%@include file="../template/resource-script.jsp" %>

<script src="${pageContext.request.contextPath}/resources/js/model/login-model.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/view/login-view.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/octopus/login-octopus.js"></script>


<script type="text/javascript">
    jQuery(document).ready(function() {
        $ = jQuery;
        loginOctopus.init();
    } );
</script>

</html>
