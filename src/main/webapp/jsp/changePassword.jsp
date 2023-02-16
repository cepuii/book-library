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

    <jsp:include page="/jsp/fragments/showResult.jsp"/>

    <div class="container">
        <ctg:showMessage error="${requestScope.reports.badPasswords}"/>
        <div class="container justify-content-center">
            <form method="post" action="${pageContext.request.contextPath}/controller">
                <input type="hidden" name="command" value="change_password">
                <label>
                    <ctg:showMessage error="${requestScope.reports.badOldPassword}"/>
                    <fmt:message key="users.password.old"/>:
                    <input class="form-control" type="password" name="oldPassword" id="password"
                           title="<fmt:message key="users.password.required"/>"
                           pattern="^(?=.*[a-zA-Z])(?=.*[0-9]).{4,20}$" required>
                </label>
                <div class="mb-1">
                    <label>
                        <fmt:message key="users.password.new"/>:
                        <input class="form-control" type="password" name="newPassword" id="new_password"
                               title="<fmt:message key="users.password.required"/>"
                               pattern="^(?=.*[a-zA-Z])(?=.*[0-9]).{4,20}$" required>
                        <ctg:showMessage error="${requestScope.reports.badPassword}"/>
                    </label>
                </div>
                <div class="mb-auto">
                    <label>
                        <fmt:message key="users.password.confirm"/>:
                        <input class="form-control" type="password" name="confirmPassword" id="confirm_password"
                               pattern="^(?=.*[a-zA-Z])(?=.*[0-9]).{4,20}$"
                               title="<fmt:message key="users.password.confirm"/>" required>
                        <ctg:showMessage error="${requestScope.reports.badConfirm}"/>
                    </label>
                </div>
                <div class="mb-auto">
                    <label class="form-check-label" for="flexCheckDefault"><fmt:message key="users.password.show"/>
                        <input class="form-check-input" type="checkbox" id="flexCheckDefault"
                               onclick="show('password');show('new_password');show('confirm_password');">
                    </label>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary"><fmt:message key="change.password"/></button>
                <button onclick="window.history.back()" type="button" class="btn btn-outline-primary">
                    <fmt:message key="books.filter.cansel"/>
                </button>
            </form>

        </div>
    </div>

    <jsp:include page="/jsp/fragments/footer.jsp"/>

</div>
<script src="${pageContext.request.contextPath}/js/showPass.js"></script>
</body>
</html>
