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
        <form method="post" action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="sign_up">
            <input type="hidden" name="userId" value="0">
            <div class="mb-1">
                <label>
                    <fmt:message key="users.email"/>*:
                    <ctg:showMessage error="${requestScope.emailExist}"/>
                    <input class="form-control" type="email" name="email"
                           pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$" required
                           value="${requestScope.userEmail}">
                </label>
            </div>
            <div class="mb-1">
                <label>
                    <fmt:message key="users.password"/>*:
                    <ctg:showMessage error="${requestScope.badPassword}"/>
                    <input class="form-control" type="password" name="password" id="password"
                           title="<fmt:message key="users.password.required"/>"
                           pattern="^(?=.*[a-zA-Z])(?=.*[0-9]).{4,20}$" required>
                </label>

            </div>
            <div class="mb-auto">
                <label>
                    <fmt:message key="users.password.confirm"/>*:
                    <ctg:showMessage error="${requestScope.badConfirm}"/>
                    <input class="form-control" type="password" name="confirmPassword" id="confirm_password"
                           pattern="^(?=.*[a-zA-Z])(?=.*[0-9]).{4,20}$"
                           title="<fmt:message key="users.password.confirm"/>" required>

                </label>
            </div>
            <div class="mb-auto">
                <label class="form-check-label" for="flexCheckDefault"><fmt:message key="users.password.show"/>
                    <input class="form-check-input" type="checkbox" id="flexCheckDefault"
                           onclick="show();">
                </label>
            </div>
            <br/>
            <br/>
            <button type="submit" class="btn btn-outline-primary"><fmt:message key="header.signup"/></button>
        </form>
    </div>

    <script src="../js/showPass.js"></script>
    <jsp:include page="/jsp/fragments/footer.jsp"/>
</div>
</body>
</html>

