<%@ include file="/jsp/fragments/pageSettings.jspf" %>
<%@ include file="/jsp/fragments/taglibs.jspf" %>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/fragments/headTag.jspf"/>
    <title><fmt:message key="header.signup"/></title>
</head>
<body>

<div class="container">

    <jsp:include page="/jsp/fragments/bodyHeader.jsp"/>


    <div class="container">
        <h5><fmt:message key="header.signup"/></h5>
        <hr>
        <form name="SignUp" method="post">
            <input type="hidden" name="command" value="sign_up">
            <div class="mb-auto">

                <label>
                    <fmt:message key="users.email"/>*:
                    <input class="form-control" type="email" name="email"
                           pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$" required
                           value="${requestScope.user.email}">
                </label>

            </div>
            <div class="mb-auto">
                <label>
                    <fmt:message key="users.password"/>*:
                    <input class="form-control" type="password" name="password" id="password"
                           title="<fmt:message key="users.password.required"/>"
                           pattern="^(?=.*[a-zA-Z])(?=.*[0-9]).{4,20}$" required>
                </label>
            </div>
            <div class="mb-auto">
                <label>
                    <fmt:message key="users.confirm.password"/>*:
                    <input class="form-control" type="password" name="confirm-password" id="confirm-password"
                           pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,20}$"
                           title="<fmt:message key="users.password.confirm"/>" required>
                    <br>
                </label>
            </div>
            <div class="mb-auto">
                <label class="form-check-label" for="flexCheckDefault"><fmt:message key="users.password.show"/>
                    <input class="form-check-input" type="checkbox" id="flexCheckDefault"
                           onclick="show();">
                </label>
            </div>
            <br/>
            ${requestScope.errorLoginPassMessage}
            ${requestScope.wrongAction}
            ${requestScope.nullPage}
            <br/>
            <input type="submit" value="Sign up"/>
        </form>
    </div>

    <script src="${pageContext.request.contextPath}/js/showPass.js"></script>

</body>
</html>

