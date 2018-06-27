<%--
  Created by IntelliJ IDEA.
  User: NAMBACH
  Date: 6/27/2018
  Time: 2:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>User Management</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <%@include file="../template/resource-stylesheet.jsp" %>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/lib/jquery-bootgrid-1-3-1/jquery.bootgrid.min.css">
</head>
<body>

<!-- Left Panel -->
<%@include file="../template/side-bar.jsp" %>
<!-- ./Left Panel -->

<!-- Main Panel -->
<div id="right-panel" class="right-panel">

    <!-- Header-->
    <header id="header" class="header">

        <div class="header-menu">

            <div class="col-sm-7">
                <a id="menuToggle" class="menutoggle pull-left"><i class="fa fa fa-tasks"></i></a>
                <div class="header-left">
                    <button class="search-trigger"><i class="fa fa-search"></i></button>
                    <div class="form-inline">
                        <form class="search-form">
                            <input class="form-control mr-sm-2" type="text" placeholder="Search ..."
                                   aria-label="Search">
                            <button class="search-close" type="submit"><i class="fa fa-close"></i></button>
                        </form>
                    </div>
                </div>
            </div>

            <div class="col-sm-5">
                <div class="user-area dropdown float-right">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
                       aria-expanded="false">
                        <img class="user-avatar rounded-circle"
                             src="${pageContext.request.contextPath}/resources/template/suffee-admin/images/admin.jpg"
                             alt="User Avatar">
                    </a>

                    <div class="user-menu dropdown-menu">
                        <a class="nav-link" href="#"><i class="fa fa- user"></i>My Profile</a>

                        <a class="nav-link" href="#"><i class="fa fa-power -off"></i>Logout</a>
                    </div>
                </div>

                <div class="language-select dropdown" id="language-select">
                    <a class="dropdown-toggle" href="#" data-toggle="dropdown" id="language" aria-haspopup="true"
                       aria-expanded="true">
                        <i class="flag-icon flag-icon-us"></i>
                    </a>
                    <div class="dropdown-menu" aria-labelledby="language">
                        <div class="dropdown-item">
                            <span class="flag-icon flag-icon-vn"></span>
                        </div>
                    </div>
                </div>

            </div>
        </div>

    </header><!-- /header -->
    <!-- Header-->

    <div class="breadcrumbs">
        <div class="col-sm-4">
            <div class="page-header float-left">
                <div class="page-title">
                    <h1>User Management</h1>
                </div>
            </div>
        </div>
        <div class="col-sm-8">
            <div class="page-header float-right">
                <div class="page-title">
                    <ol class="breadcrumb text-right">
                        <li><a href="#">Dashboard</a></li>
                        <li class="active">User Management</li>
                    </ol>
                </div>
            </div>
        </div>
    </div>

    <div class="content mt-3">
        <div class="animated fadeIn">
            <div class="row">

                <div class="col-md-12">
                    <div class="card">
                        <div class="card-header">
                            <strong class="card-title">Data Table</strong>
                        </div>
                        <div class="card-body">


                            <!-- Table -->
                            <table id="user-table" class="table table-condensed table-hover table-striped">
                                <thead>
                                <tr>
                                    <th data-column-id="username" data-type="numeric">Username</th>
                                    <th data-column-id="name">Full Name</th>
                                    <th data-column-id="info" data-order="desc">Infomation</th>
                                    <th data-column-id="commands" data-formatter="commands" data-sortable="false">Commands</th>
                                </tr>
                                </thead>
                            </table>
                            <!-- ./Table -->


                        </div>
                    </div>
                </div>


            </div>
        </div><!-- .animated -->
    </div><!-- .content -->
</div><!-- /#right-panel -->

<!-- ./Main Panel -->
</body>
<%@include file="../template/resource-script.jsp" %>

<script src="${pageContext.request.contextPath}/resources/lib/jquery-bootgrid-1-3-1/jquery.bootgrid.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/model/user-model.js"></script>


<script src="${pageContext.request.contextPath}/resources/js/model/user-model.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/view/user-view.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/octopus/user-octopus.js"></script>


<script type="text/javascript">
    jQuery(document).ready(function() {
        $ = jQuery;
        userOctopus.init();
    } );
</script>

</html>


