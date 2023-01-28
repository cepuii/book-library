<%@ include file="/jsp/fragments/pageSettings.jsp" %>
<%@ include file="/jsp/fragments/taglibs.jsp" %>

<!doctype html>
<html lang="en">
<head>
    <title><fmt:message key="header.login"/></title>
    <jsp:include page="/jsp/fragments/headTag.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="/jsp/fragments/bodyHeader.jsp"/>
    <div class="container p-5 my-5 border">
        <h5><fmt:message key="greeting"/></h5>
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
            <div class="mb-auto">
                <label class="form-check-label" for="flexCheckDefault"><fmt:message key="users.password.show"/>
                    <input class="form-check-input" type="checkbox" id="flexCheckDefault"
                           onclick="show();">
                </label>
            </div>

            <br/>
            <input type="submit" class="btn btn-primary"
                   formaction="${pageContext.request.contextPath}/controller?command=login"
                   value="<fmt:message key="header.login"/>"/>

            <button onclick="window.history.back()" type="button" class="btn btn-outline-primary">
                <fmt:message key="books.filter.cansel"/>
            </button>
        </form>
    </div>
    <script src="${pageContext.request.contextPath}/js/scripts.js"></script>
    <jsp:include page="/jsp/fragments/footer.jsp"/>
</div>
</body>
</html>
