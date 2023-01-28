<%@ include file="/jsp/fragments/pageSettings.jsp" %>
<%@ include file="/jsp/fragments/taglibs.jsp" %>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/fragments/headTag.jsp"/>
    <title><fmt:message key="books.edit.add.librarian"/></title>
</head>
<body>

<div class="container">

    <jsp:include page="/jsp/fragments/bodyHeader.jsp"/>

    <jsp:include page="/jsp/fragments/showResult.jsp"/>
    <ctg:showMessage error="${requestScope.wrongAction}"/>
    <div class="container">
        <form class="g-3" method="post" action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="save_user">
            <div class="md-6">
                <label for="inputEmail4" class="form-label"><fmt:message key="users.add.email"/>
                    <input type="hidden" name="userId" value="0">
                    <input type="email" class="form-control" id="inputEmail4" name="email"
                           value="${requestScope.newUser.email}" onfocus="clearClass('email')"
                           onblur="validate('email')">
                    <div class="valid-feedback">
                        <fmt:message key="ok"/>
                    </div>
                    <div class="invalid-feedback">
                        <fmt:message key="message.signUp.email.exist"/>
                    </div>
                </label>
            </div>
            <br/>
            <div class="md-6">
                <label for="inputPassword4" class="form-label"><fmt:message key="users.add.password"/>
                    <input type="password" class="form-control" id="inputPassword4" name="password"
                           placeholder="Enter password">
                </label>
            </div>
            <br/>
            <div class="mb-auto">
                <label for="inputAddress" class="form-label"><fmt:message key="users.role"/>
                    <select name="role" id="inputAddress">
                        <option value="LIBRARIAN" selected><fmt:message key="users.filter.role.librarian"/></option>
                    </select>
                </label>
            </div>
            <br/>
            <div class="mb-auto">
                <label class="form-check-label" for="flexCheckDefault"><fmt:message key="users.password.show"/>
                    <input class="form-check-input" type="checkbox" id="flexCheckDefault"
                           onclick="show();">
                </label>
            </div>
            <br/>
            <div class="col-12">
                <button type="submit" class="btn btn-primary"><fmt:message key="save"/></button>
            </div>
        </form>
    </div>
    <jsp:include page="/jsp/fragments/showResult.jsp"/>
    <jsp:include page="/jsp/fragments/footer.jsp"/>
</div>
<script src="${pageContext.request.contextPath}/js/validateField.js"></script>
<script src="${pageContext.request.contextPath}/js/showPass.js"></script>
</body>
</html>
