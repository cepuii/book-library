<%@ include file="/jsp/fragments/pageSettings.jspf" %>
<%@ include file="/jsp/fragments/taglibs.jspf" %>

<!doctype html>
<html lang="en">
<head>
    <title><fmt:message key="header.login"/></title>
    <jsp:include page="/jsp/fragments/headTag.jspf"/>
</head>
<body>
<jsp:include page="/jsp/fragments/bodyHeader.jsp"/>
<div class="container p-5 my-5 border">
    <h5>Welcome to the library</h5>
    <hr>
    <ctg:showMessage error="${requestScope.errorLoginPassMessage}"/>
    <form name="LoginForm" method="post">
        <input type="hidden" name="command" value="sign_up">
        <div class="mb-1">
            <label>
                <fmt:message key="users.email"/>:
                <input class="form-control" type="email" name="email"
                       pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$" required
                       value="${sessionScope.userEmail}">
            </label>
        </div>
        <div class="mb-1">
            <label>
                <fmt:message key="users.password"/>:
                <input class="form-control" type="password" name="password" id="password"
                       title="<fmt:message key="users.password.required"/>"
                       pattern="^(?=.*[a-zA-Z])(?=.*[0-9]).{4,20}$" required>
            </label>

        </div>


        <br/>
        <input type="submit"
               formaction="${pageContext.request.contextPath}/controller?command=login"
               value="Login"/>
        <input type="submit"
               formaction="${pageContext.request.contextPath}/controller?command=sign_up"
               value="Sign up"/>
        <%--            <input type="submit" value="Continue without authorisation" >--%>
    </form>
</div>
</body>
</html>
