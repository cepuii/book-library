<%--
  Created by IntelliJ IDEA.
  User: cepuii
  Date: 12/8/2022
  Time: 9:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
<div class="container">
    <h5>Добро пожаловать в библиотеку</h5>
    <hr>
    <form name="LoginForm" method="post" action="controller">
        <input type="hidden" name="command" value="login">
        Login: <input type="text" name="login" value=""/>
        <br/>
        Password: <input type="password" name="password" value=""/>
        <br/>
        ${errorLoginPassMessage}
        ${wrongAction}
        ${nullPage}
        <br/>
        <input type="submit" value="Log in"/>
        <%--            <input type="submit" value="Continue without authorisation" >--%>
    </form>
</div>
</body>
</html>
