<%@ include file="/jsp/fragments/pageSettings.jsp" %>
<%@ include file="/jsp/fragments/taglibs.jsp" %>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/fragments/headTag.jsp"/>
    <title><fmt:message key="profile"/></title>
</head>
<body>
<div class="container">
    <jsp:include page="/jsp/fragments/bodyHeader.jsp"/>

    <jsp:include page="/jsp/fragments/showResult.jsp"/>
    <div class="container">
        <h5><fmt:message key="profile"/></h5>
        <hr>
        <form class="row g-3" method="post" action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="save_user_change">
            <input type="hidden" name="userId" value="${requestScope.user.id}">
            <div class="mb-1">
                <label class="form-label" for="email">
                    <fmt:message key="users.email"/>:
                    <ctg:showMessage error="${sessionScope.emailExist}"/>
                    <input class="form-control" id="email" type="email" name="email" value="${requestScope.user.email}"
                           pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$" required
                           onfocus="clearClass('email')" onchange="validate('email')">
                    <div class="valid-feedback">
                        <fmt:message key="ok"/>
                    </div>
                    <div class="invalid-feedback">
                        <fmt:message key="message.signUp.email.exist"/>
                    </div>
                </label>
            </div>
            <div class="mb-1">
                <label class="form-label">
                    <fmt:message key="users.registered"/>:
                    <input class="form-control" type="text" name="registered"
                           value="<fmt:formatDate dateStyle="long" value="${requestScope.user.registered}"/>"
                           disabled>
                </label>

            </div>
            <div class="mb-1">
                <label class="form-label">
                    <fmt:message key="users.blocked"/>:
                    <input class="form-control" type="text" name="blocked"
                           value="${requestScope.user.blocked}" disabled>
                </label>

            </div>
            <div class="mb-1">
                <label class="form-label">
                    <fmt:message key="users.fine"/>:
                    <input class="form-control" type="text" name="fine"
                           value="${requestScope.user.fine}" disabled>
                </label>
            </div>
            <div class="mb-1">
                <label class="form-label">
                    <fmt:message key="users.role"/>:
                    <input class="form-control" type="text" name="role"
                           value="${requestScope.user.role}" disabled>
                </label>
            </div>
            <br/>
            <br/>
            <label class="form-label">
                <button type="submit" class="btn btn-primary"><fmt:message key="save"/></button>
                <button onclick="window.history.back()" type="button" class="btn btn-outline-primary">
                    <fmt:message key="books.filter.cansel"/>
                </button>
            </label>
        </form>
        <form name="ChangePassword" action="${pageContext.request.contextPath}/controller" method="get">
            <input type="hidden" name="command" value="change_password">
            <label class="form-label">
                <button type="submit" class="btn btn-outline-primary"><fmt:message key="change.password"/></button>
            </label>
        </form>
        <br>
        <hr/>
        <br>
        <c:if test="${sessionScope.userRole eq 'READER'}">
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

            <ul class="pagination justify-content-center" style="margin:20px 0">

                <c:if test="${sessionScope.page.currentPage ne 1}">
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/controller?command=show_profile&currentPage=${sessionScope.page.currentPage-1}">
                            <fmt:message key="main.prev"/></a>
                    </li>
                </c:if>
                <c:forEach begin="${sessionScope.page.currentPage > 2 ? sessionScope.page.currentPage - 1 : 1}"
                           end="${sessionScope.page.pageAmount > 5 ? 5 : sessionScope.page.pageAmount}"
                           varStatus="loop">
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/controller?command=show_profile&currentPage=${loop.index}">
                                ${loop.index}</a>
                    </li>
                </c:forEach>
                <c:if test="${sessionScope.page.pageAmount ne 0 || sessionScope.page.currentPage ne sessionScope.page.pageAmount}">
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/controller?command=show_profile&currentPage=${sessionScope.page.currentPage+1}">
                            <fmt:message key="main.next"/></a>
                    </li>
                </c:if>
            </ul>
        </c:if>
    </div>
    <jsp:include page="/jsp/fragments/footer.jsp"/>
</div>

<script src="${pageContext.request.contextPath}/js/validateField.js"></script>

</body>
</html>
