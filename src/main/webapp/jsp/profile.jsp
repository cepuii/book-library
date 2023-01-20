<%@ include file="/jsp/fragments/pageSettings.jspf" %>
<%@ include file="/jsp/fragments/taglibs.jspf" %>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/fragments/headTag.jspf"/>
    <title><fmt:message key="profile"/></title>
</head>

<body>

<div class="container">
    <jsp:include page="/jsp/fragments/bodyHeader.jsp"/>

    <jsp:include page="/jsp/fragments/showResult.jsp"/>
    <div class="container">
        <h5><fmt:message key="profile"/></h5>
        <hr>
        <form method="post" action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="save_user_change">
            <input type="hidden" name="userId" value="${requestScope.user.id}">
            <div class="mb-1">
                <label>
                    <fmt:message key="users.email"/>:
                    <ctg:showMessage error="${sessionScope.emailExist}"/>
                    <input class="form-control" type="email" name="email"
                           pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$" required
                           value="${requestScope.user.email}">
                </label>
            </div>
            <div class="mb-1">
                <label>
                    <fmt:message key="users.registered"/>:
                    <input class="form-control" type="text" name="registered"
                           value="${requestScope.user.registered}" disabled>
                </label>

            </div>
            <div class="mb-1">
                <label>
                    <fmt:message key="users.blocked"/>:
                    <input class="form-control" type="text" name="blocked"
                           value="${requestScope.user.blocked}" disabled>
                </label>

            </div>
            <div class="mb-1">
                <label>
                    <fmt:message key="users.fine"/>:
                    <input class="form-control" type="text" name="fine"
                           value="${requestScope.user.fine}" disabled>
                </label>
            </div>
            <div class="mb-1">
                <label>
                    <fmt:message key="users.role"/>:
                    <input class="form-control" type="text" name="role"
                           value="${requestScope.user.role}" disabled>
                </label>
            </div>
            <br/>
            <br/>
            <button type="submit" class="btn btn-outline-primary"><fmt:message key="save"/></button>
            <%--            TODO cansel button--%>
        </form>
        <form name="ChangePassword" action="${pageContext.request.contextPath}/jsp/changePassword.jsp" method="post">
            <button type="submit" class="btn btn-outline-primary"><fmt:message key="change.password"/></button>
        </form>
    </div>
    <br>
    <hr/>
    <br>
    <table class="table table-dark table-striped">
        <caption><fmt:message key="orders.history"/></caption>
        <thead>
        <tr>
            <th><fmt:message key="loan.book"/></th>
            <th><fmt:message key="loan.status"/></th>
            <th><fmt:message key="loan.start"/></th>
            <th><fmt:message key="loan.end"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="loan" items="${requestScope.loans}">
            <tr>
                <td>${loan.bookInfo}</td>
                <td>${loan.status}</td>
                <td><fmt:formatDate type="date" dateStyle="long" value="${loan.startDate}"/></td>
                <td><fmt:formatDate type="date" dateStyle="long" value="${loan.endDate}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>


</div>
</body>
</html>
