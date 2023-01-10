<%--
  Created by IntelliJ IDEA.
  User: cepuii
  Date: 12/8/2022
  Time: 8:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error info</title>
</head>
<body>
<div class="container">
    <hr/>
    Request from ${pageContext.errorData.requestURI} is failed
    <br/>
    Servlet name or type: ${pageContext.errorData.servletName}
    <br/>
    Status code: ${pageContext.errorData.statusCode}
    <br/>
    Exception: ${pageContext.errorData.throwable}
</div>
</body>
</html>
