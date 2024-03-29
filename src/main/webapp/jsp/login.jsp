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

        <jsp:include page="/jsp/fragments/showResult.jsp"/>

        <form name="LoginForm" method="post">
            <input type="hidden" name="command" value="sign_up">
            <div class="mb-1">
                <label>
                    <fmt:message key="users.email"/>:
                    <input class="form-control" type="email" name="email"
                           pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$" required
                           value="${requestScope.reports.userEmail}">
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
                           onclick="show('password');">
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

        <script src="https://accounts.google.com/gsi/client" async defer></script>
        <div id="g_id_onload"
             data-client_id="453363600410-g5168pvimaearhqkj1erithac4glm141.apps.googleusercontent.com"
             data-login_uri="${pageContext.request.contextPath}/controller?command=verify_token"
        <%--             data-auto_prompt="false"--%>
        >

        </div>
        <div class="g_id_signin"
             data-type="standard"
             data-size="large"
             data-theme="outline"
             data-text="sign_in_with"
             data-shape="rectangular"
             data-logo_alignment="left">
        </div>


    </div>
    <jsp:include page="/jsp/fragments/footer.jsp"/>
</div>
<script src="${pageContext.request.contextPath}/js/showPass.js"></script>
</body>
</html>
