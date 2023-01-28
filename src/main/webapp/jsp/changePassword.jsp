<%@ include file="/jsp/fragments/pageSettings.jsp" %>
<%@ include file="/jsp/fragments/taglibs.jsp" %>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/fragments/headTag.jsp"/>
    <title><fmt:message key="change.password"/></title>
</head>
<body>

<div class="container">

    <jsp:include page="/jsp/fragments/bodyHeader.jsp"/>
    <div class="container">
        <ctg:showMessage error="${requestScope.badPasswords}"/>
        <div class="container justify-content-center">
            <form method="post" action="${pageContext.request.contextPath}/controller">
                <input type="hidden" name="command" value="change_password">
                <label>
                    <ctg:showMessage error="${requestScope.badOldPassword}"/>
                    <fmt:message key="users.password.old"/>:
                    <input class="form-control" type="password" name="oldPassword" id="password"
                           title="<fmt:message key="users.password.required"/>"
                           pattern="^(?=.*[a-zA-Z])(?=.*[0-9]).{4,20}$" required>
                </label>
                <div class="mb-1">
                    <label>
                        <ctg:showMessage error="${requestScope.badPassword}"/>
                        <fmt:message key="users.password.new"/>:
                        <input class="form-control" type="password" name="newPassword" id="new_password"
                               title="<fmt:message key="users.password.required"/>"
                               pattern="^(?=.*[a-zA-Z])(?=.*[0-9]).{4,20}$" required>
                    </label>
                </div>
                <div class="mb-auto">
                    <label>
                        <ctg:showMessage error="${requestScope.badConfirm}"/>
                        <fmt:message key="users.password.confirm"/>:
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
                <button type="submit" class="btn btn-outline-primary"><fmt:message key="change.password"/></button>
                <button onclick="window.history.back()" type="button">
                    <fmt:message key="books.filter.cansel"/>
                </button>
            </form>

        </div>
    </div>

    <script src="../js/showPass.js"></script>
    <jsp:include page="/jsp/fragments/footer.jsp"/>

</div>
</body>
</html>
