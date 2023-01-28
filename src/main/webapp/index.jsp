<%--
  Created by IntelliJ IDEA.
  User: cepuii
  Date: 12/8/2022
  Time: 6:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="jsp/error/error.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Book library</title>
</head>
<body>
<%--<jsp:forward page="jsp/main.jsp"></jsp:forward>--%>
<a href="${pageContext.request.contextPath}/controller?command=show_books">go to</a>
</body>
</html>
