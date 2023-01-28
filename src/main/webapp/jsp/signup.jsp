<%@ include file="/jsp/fragments/pageSettings.jsp" %>
<%@ include file="/jsp/fragments/taglibs.jsp" %>
<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/fragments/headTag.jsp"/>
    <title><fmt:message key="header.signup"/></title>
</head>
<body>

<div class="container">

    <jsp:include page="/jsp/fragments/bodyHeader.jsp"/>

    <div class="container">
        <h5><fmt:message key="header.signup"/></h5>
        <hr>
        <form class="row g-3" id="signUpForm" method="post" action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="sign_up">
            <input type="hidden" name="userId" value="0">
            <div class="mb-1">
                <label class="form-label" for="email">
                    <fmt:message key="users.email"/>*:
                    <ctg:showMessage error="${requestScope.emailExist}"/>
                    <input class="form-control" id="email" type="email" name="email" required
                           pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$"
                           value="${requestScope.userEmail}" onfocus="clearClass('email')"
                           onblur="validate('email')"
                           title="<fmt:message key="message.signUp.email"/>)">
                    <div class="valid-feedback">
                        <fmt:message key="ok"/>
                    </div>
                    <div class="invalid-feedback">
                        <fmt:message key="message.signUp.email.exist"/>
                    </div>
                </label>
                <div id="result-email">

                </div>
            </div>
            <div class="mb-1">
                <label>
                    <fmt:message key="users.password"/>*:
                    <ctg:showMessage error="${requestScope.badPassword}"/>
                    <input class="form-control" type="password" name="password" id="password"
                           required pattern="^(?=.*[a-zA-Z])(?=.*[0-9]).{4,20}$"
                           title="<fmt:message key="users.password.required"/>)">
                </label>

            </div>
            <div class="mb-auto">
                <label>
                    <fmt:message key="users.password.confirm"/>*:
                    <ctg:showMessage error="${requestScope.badConfirm}"/>
                    <input class="form-control" type="password" name="confirmPassword" id="confirm_password" required
                           pattern="^(?=.*[a-zA-Z])(?=.*[0-9]).{4,20}$"
                           title="<fmt:message key="users.password.confirm"/>)">

                </label>
            </div>
            <div class="mb-auto">
                <label class="form-check-label" for="flexCheckDefault"><fmt:message key="users.password.show"/>
                    <input class="form-check-input" type="checkbox" id="flexCheckDefault"
                           onclose="show('password');show('confirm_password');">
                </label>
            </div>
            <br/>
            <br/>
            <label>
                <button type="submit" id="submit-button" class="btn btn-outline-primary"><fmt:message
                        key="header.signup"/></button>
            </label>
        </form>
    </div>

    <jsp:include page="/jsp/fragments/footer.jsp"/>
</div>
<script src="${pageContext.request.contextPath}/js/showPass.js"></script>
<script src="${pageContext.request.contextPath}/js/validateField.js"></script>

</body>
</html>

