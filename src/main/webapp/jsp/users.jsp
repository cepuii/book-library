<%@ include file="/jsp/frgments/pageSettings.jspf" %>
<%@ include file="/jsp/frgments/taglibs.jspf" %>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/frgments/headTag.jspf"/>
    <title><fmt:message key="users.href"/></title>
</head>
<body>

<div class="container">

    <jsp:include page="/jsp/frgments/bodyHeader.jsp"/>


    <div class="container">


        <div class="row">
            <div class="col-8">
                <form action="${pageContext.request.contextPath}/controller">
                    <div class="row">
                        <input type="hidden" name="command" value="show_users">
                        <input type="hidden" name="modified" value="true">
                        <div class="col">
                            <label>
                                <fmt:message key="users.filter.email"/>
                                <input type="text" name="userSearch" value="${sessionScope.filter.firstParam}">
                            </label>
                        </div>
                        <div class="col">
                            <label>
                                <fmt:message key="users.filter.role"/>
                                <select name="userRoleSearch">
                                    <option value="" ${sessionScope.filter.secondParam eq '' ? 'selected':''}>
                                        <fmt:message
                                                key="users.filter.role.choose"/></option>
                                    <option value="READER" ${sessionScope.filter.secondParam eq 'READER' ? 'selected':''}>
                                        <fmt:message
                                                key="users.filter.role.reader"/></option>
                                    <option value="LIBRARIAN" ${sessionScope.filter.secondParam eq 'LIBRARIAN' ? 'selected':''}>
                                        <fmt:message
                                                key="users.filter.role.librarian"/></option>
                                    <option value="ADMIN" ${sessionScope.filter.secondParam eq 'ADMIN' ? 'selected':''}>
                                        <fmt:message
                                                key="users.filter.role.admin"/></option>
                                </select>
                            </label>
                        </div>
                        <div class="col">
                            <label>
                                <fmt:message key="books.sort.name"/>
                                <select name="orderBy">
                                    <option value="email" ${sessionScope.filter.orderBy eq 'email' ? 'selected':''}>
                                        <fmt:message
                                                key="users.email"/></option>
                                    <option value="registered" ${sessionScope.filter.orderBy eq 'registered' ? 'selected':''}>
                                        <fmt:message
                                                key="users.registered"/></option>
                                    <option value="fine" ${sessionScope.filter.orderBy eq 'fine' ? 'selected':''}>
                                        <fmt:message
                                                key="users.fine"/></option>

                                </select>
                            </label>
                        </div>
                        <div class="col">
                            <label>
                                <fmt:message key="books.order"/>
                                <select name="descending">
                                    <option value="false" ${sessionScope.filter.descending eq "false" ? "selected" : ""}>
                                        <fmt:message
                                                key="books.sort.asc"/></option>
                                    <option value="true" ${sessionScope.filter.descending eq "true" ? "selected" : ""}>
                                        <fmt:message
                                                key="books.sort.desc"/></option>
                                </select>
                            </label>
                        </div>
                        <div class="col">
                            <button type="submit" class="btn btn-primary"><fmt:message
                                    key="books.filter.button"/></button>
                        </div>
                    </div>
                </form>
            </div>
            <%--        cancel button--%>
            <div class="col">
                <form action="${pageContext.request.contextPath}/controller">
                    <input type="hidden" name="command" value="show_users">
                    <input type="hidden" name="modified" value="true">
                    <input type="hidden" name="cleanFilter" value="true">
                    <button type="submit" class="btn btn-outline-primary"><fmt:message
                            key="books.filter.cansel"/></button>
                </form>
            </div>
            <c:if test="${sessionScope.userRole eq 'ADMIN'}">
                <div class="col">
                    <form action="${pageContext.request.contextPath}/controller">
                        <input type="hidden" name="command" value="add_librarian">
                        <input type="hidden" name="bookId" value="0">
                        <button type="submit" class="btn btn-primary"><fmt:message
                                key="books.edit.add.librarian"/></button>
                    </form>
                </div>
            </c:if>
        </div>
        <hr/>

        <%--        books catalog--%>
        <table class="table table-dark table-striped">
            <caption><fmt:message key="users.href"/></caption>
            <thead>
            <tr>
                <th><fmt:message key="users.email"/></th>
                <th><fmt:message key="users.registered"/></th>
                <th><fmt:message key="users.role"/></th>
                <th><fmt:message key="users.fine"/></th>
                <th><fmt:message key="users.blocked"/></th>
            </tr>
            </thead>
            <tbody>

            <%--            start loop by books--%>
            <c:forEach var="user" items="${requestScope.users}">
                <tr>
                    <td>${user.email}</td>
                    <td>${user.registered}</td>
                    <td>${user.role}</td>
                    <td>${user.fine}</td>
                    <td>
                        <div class="row">
                            <div class="col-4">
                                    ${user.blocked}
                            </div>
                            <div class="col-6">
                                <form name="BlockUser" method="post"
                                      action="${pageContext.request.contextPath}/controller">
                                    <input type="hidden" name="command"
                                           value="block_user">
                                    <input type="hidden" name="userId" value="${user.id}">
                                    <c:choose>
                                        <c:when test="${user.blocked}">
                                            <input type="hidden" name="isBlocked" value="false">
                                            <c:set var="blockAction">
                                                <fmt:message key="users.enable"/>
                                            </c:set>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden" name="isBlocked" value="true">
                                            <c:set var="blockAction">
                                                <fmt:message key="users.block"/>
                                            </c:set>
                                        </c:otherwise>
                                    </c:choose>
                                    <button class="btn btn-outline-primary" type="submit">
                                            ${blockAction}
                                    </button>
                                </form>
                            </div>
                        </div>

                    </td>
                        <%--                    <td>--%>
                        <%--                        <c:choose>--%>
                        <%--                            &lt;%&ndash;buttons for READER&ndash;%&gt;--%>
                        <%--                            <c:when test="${sessionScope.userRole eq 'READER'}">--%>
                        <%--                                <c:set var="testContain" scope="page">--%>
                        <%--                                    <ctg:isContain bookId="${user.id}"/>--%>
                        <%--                                </c:set>--%>
                        <%--                                <c:choose>--%>
                        <%--                                    <c:when test="${testContain}">--%>
                        <%--                                        <form name="RemoveBookFromOrder" method="post"--%>
                        <%--                                              action="${pageContext.request.contextPath}/controller">--%>
                        <%--                                            <input type="hidden" name="command"--%>
                        <%--                                                   value="remove_book_from_order">--%>
                        <%--                                            <input type="hidden" name="bookId" value="${user.id}">--%>
                        <%--                                            <button class="btn-sm" type="submit">--%>
                        <%--                                                <small>--%>
                        <%--                                                    <fmt:message key="books.order.inOrder"/>--%>
                        <%--                                                </small>--%>
                        <%--                                            </button>--%>
                        <%--                                        </form>--%>
                        <%--                                    </c:when>--%>
                        <%--                                    <c:otherwise>--%>
                        <%--                                        <form name="AddBookToOrder" method="post"--%>
                        <%--                                              action="${pageContext.request.contextPath}/controller">--%>
                        <%--                                            <input type="hidden" name="command" value="add_book_to_order">--%>
                        <%--                                            <input type="hidden" name="bookId" value="${user.id}">--%>
                        <%--                                            <input type="hidden" name="book" value="${user}">--%>
                        <%--                                            <div class="input-group-sm">--%>
                        <%--                                                <div class="row">--%>
                        <%--                                                    <div class="col">--%>
                        <%--                                                        <label>--%>
                        <%--                                                            <select class="form-select-sm" name="days">--%>
                        <%--                                                                <option selected><fmt:message--%>
                        <%--                                                                        key="books.order.choose"/></option>--%>
                        <%--                                                                <option value="1"><fmt:message--%>
                        <%--                                                                        key="books.order.oneDay"/></option>--%>
                        <%--                                                                <option value="7"><fmt:message--%>
                        <%--                                                                        key="books.order.oneWeek"/></option>--%>
                        <%--                                                                <option value="30"><fmt:message--%>
                        <%--                                                                        key="books.order.oneMonth"/></option>--%>
                        <%--                                                            </select>--%>
                        <%--                                                        </label>--%>
                        <%--                                                    </div>--%>
                        <%--                                                    <div class="col">--%>
                        <%--                                                        <button class="btn-sm" type="submit">--%>
                        <%--                                                            <small>--%>
                        <%--                                                                <fmt:message key="books.order.button"/>--%>
                        <%--                                                            </small>--%>
                        <%--                                                        </button>--%>
                        <%--                                                    </div>--%>
                        <%--                                                </div>--%>
                        <%--                                            </div>--%>
                        <%--                                        </form>--%>
                        <%--                                    </c:otherwise>--%>
                        <%--                                </c:choose>--%>
                        <%--                            </c:when>--%>
                        <%--                            &lt;%&ndash;buttons for ADMIN&ndash;%&gt;--%>
                        <%--                            <c:when test="${sessionScope.userRole eq 'ADMIN'}">--%>
                        <%--                                <div class="row">--%>
                        <%--                                    <div class="col">--%>
                        <%--                                        <form name="RemoveBook" method="post"--%>
                        <%--                                              action="${pageContext.request.contextPath}/controller">--%>
                        <%--                                            <input type="hidden" name="command"--%>
                        <%--                                                   value="remove_book">--%>
                        <%--                                            <input type="hidden" name="bookId" value="${user.id}">--%>
                        <%--                                            <button class="btn btn-outline-primary" type="submit">--%>
                        <%--                                                <small>--%>
                        <%--                                                    <fmt:message key="books.catalog.remove"/>--%>
                        <%--                                                </small>--%>
                        <%--                                            </button>--%>
                        <%--                                        </form>--%>
                        <%--                                    </div>--%>
                        <%--                                    <div class="col">--%>
                        <%--                                        <form name="EditBook" method="get"--%>
                        <%--                                              action="${pageContext.request.contextPath}/controller">--%>
                        <%--                                            <input type="hidden" name="bookId" value="${user.id}">--%>
                        <%--                                            <input type="hidden" name="command" value="edit_book">--%>
                        <%--                                            <button type="submit" class="btn btn-primary">--%>
                        <%--                                                <fmt:message key="books.catalog.edit"/>--%>
                        <%--                                            </button>--%>
                        <%--                                        </form>--%>
                        <%--                                    </div>--%>
                        <%--                                </div>--%>
                        <%--                            </c:when>--%>
                        <%--                        </c:choose>--%>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <%--        TODO first prev next last  --%>
        <ul class="pagination justify-content-center" style="margin:20px 0">

            <c:if test="${sessionScope.page.currentPage ne 1}">
                <li class="page-item">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/controller?command=show_users&currentPage=${sessionScope.page.currentPage-1}">
                        <fmt:message key="main.prev"/></a>
                </li>
            </c:if>
            <c:forEach begin="1" end="${sessionScope.page.pageAmount}" varStatus="loop">
                <li class="page-item">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/controller?command=show_users&currentPage=${loop.index}">
                            ${loop.index}</a>
                </li>
            </c:forEach>
            <c:if test="${sessionScope.page.currentPage ne sessionScope.page.pageAmount}">
                <li class="page-item">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/controller?command=show_users&currentPage=${sessionScope.page.currentPage+1}">
                        <fmt:message key="main.next"/></a>
                </li>
            </c:if>
        </ul>
    </div>
</div>
</div>
<hr/>
<c:if test="${not empty param.success}">
    <div class="alert alert-success d-flex align-items-center" role="alertdialog">
        <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Danger:">
        </svg>
        <div>
                ${param.success}
        </div>
    </div>
</c:if>

</body>
</html>
