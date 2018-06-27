<%--
  Created by IntelliJ IDEA.
  User: NAMBACH
  Date: 6/27/2018
  Time: 1:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Habit Portal</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <%@include file="../template/resource-stylesheet.jsp" %>
</head>
<body>

<!-- Left Panel -->
<%@include file="../template/side-bar.jsp" %>
<!-- Left Panel -->


<%@include file="../template/right-panel.jsp" %>

</body>
<%@include file="../template/resource-script.jsp" %>

<script src="${pageContext.request.contextPath}/resources/template/suffee-admin/assets/js/lib/chart-js/Chart.bundle.js"></script>

<script src="${pageContext.request.contextPath}/resources/template/suffee-admin/assets/js/widgets.js"></script>
<script src="${pageContext.request.contextPath}/resources/template/suffee-admin/assets/js/dashboard.js"></script>

<script src="${pageContext.request.contextPath}/resources/template/suffee-admin/assets/js/lib/vector-map/jquery.vmap.js"></script>
<script src="${pageContext.request.contextPath}/resources/template/suffee-admin/assets/js/lib/vector-map/jquery.vmap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/template/suffee-admin/assets/js/lib/vector-map/jquery.vmap.sampledata.js"></script>
<script src="${pageContext.request.contextPath}/resources/template/suffee-admin/assets/js/lib/vector-map/country/jquery.vmap.world.js"></script>

<script>
    document.addEventListener("DOMContentLoaded", function(event) {
        ( function ( $ ) {
            "use strict";

            jQuery( '#vmap' ).vectorMap( {
                map: 'world_en',
                backgroundColor: null,
                color: '#ffffff',
                hoverOpacity: 0.7,
                selectedColor: '#1de9b6',
                enableZoom: true,
                showTooltip: true,
                values: sample_data,
                scaleColors: [ '#1de9b6', '#03a9f5' ],
                normalizeFunction: 'polynomial'
            } );
        } )( jQuery );
    });
</script>
</html>

