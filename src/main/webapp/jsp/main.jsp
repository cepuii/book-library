<%--
  Created by IntelliJ IDEA.
  User: cepuii
  Date: 12/8/2022
  Time: 9:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/jsp/frgments/pageSettings.jspf" %>
<%@ include file="/jsp/frgments/taglibs.jspf" %>
<fmt:setBundle basename="text"/>
<fmt:setLocale value="${sessionScope.lang}" scope="session"/>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/frgments/headTag.jspf"/>
    <title><fmt:message key="main.title"/></title>
</head>
<body>

<div class="container">

    <jsp:include page="/jsp/frgments/bodyHeader.jsp"/>


    <div class="container">

        Books catalog
        <c:if test="${empty sessionScope.books}">
            <jsp:include page="/controller?command=empty_command&currentPage=1"/>
        </c:if>
        <div>
            <%--            https://getbootstrap.su/docs/5.0/forms/layout/--%>
            <form action="controller?command=empty_command">
                <input type="hidden" name="modified" value="true">
                <label>
                    <fmt:message key="books.filter.title"/>
                    <input type="text" name="titleSearch" value="${sessionScope.filter.title}">
                </label>
                <label>
                    <fmt:message key="books.filter.author"/>
                    <input type="text" name="authorSearch" value="${sessionScope.filter.author}">
                    <%--                        <button type="submit"><fmt:message key="books.sort.button"/></button>--%>
                </label>
                <%--                </div>--%>
                <%--                <div class="col"><fmt:message key="books.sort.name"/>--%>
                <select name="orderBy">
                    <option value="b_title" ${sessionScope.filter.orderBy eq 'b_title' ? 'selected':''}><fmt:message
                            key="books.sort.title"/></option>
                    <option value="pt_name" ${sessionScope.filter.orderBy eq 'pt_name' ? 'selected':''}><fmt:message
                            key="books.sort.type"/></option>
                    <option value="b_date" ${sessionScope.filter.orderBy eq 'b_date' ? 'selected':''}><fmt:message
                            key="books.sort.date"/></option>
                    <option value="authors" ${sessionScope.filter.orderBy eq 'authors' ? 'selected':''}><fmt:message
                            key="books.sort.author"/></option>
                </select>
                <select name="descending">
                    <option value="false" ${sessionScope.filter.descending eq "false" ? "selected" : ""}>
                        <fmt:message
                                key="books.sort.asc"/></option>
                    <option value="true" ${sessionScope.filter.descending eq "true" ? "selected" : ""}><fmt:message
                            key="books.sort.desc"/></option>
                </select>
                <button type="submit"><fmt:message key="books.filter.button"/></button>
            </form>
        </div>
        <div class="justify-content-end">
            <form action="controller?command=empty_command">
                <input type="hidden" name="modified" value="true">
                <input type="hidden" name="cleanFilter" value="true">
                <button type="submit"><fmt:message key="books.filter.cansel"/></button>
            </form>
        </div>
        <hr/>
        <table class="table table-dark table-striped">
            <caption><fmt:message key="books.caption"/></caption>
            <thead>
            <tr>
                <th><fmt:message key="books.title"/></th>
                <th><fmt:message key="books.publicationType"/></th>
                <th><fmt:message key="books.datePublication"/></th>
                <th><fmt:message key="books.authors"/></th>
                <th><fmt:message key="books.order"/></th>
            </tr>
            </thead>
            <tbody>
            <%--        <jsp:useBean id="book" class="ua.od.cepuii.library.entity.Book"/>--%>
            <c:forEach var="book" items="${sessionScope.books}">
                <tr>
                    <td>${book.title}</td>
                    <td>${book.publicationType}</td>
                    <td>${book.datePublication}</td>
                    <td>${book.authorSet}</td>
                    <td>
                        <c:set var="testContain" scope="page">
                            <ctg:isContain bookId="${book.id}"/>
                        </c:set>
                        <c:choose>
                            <c:when test="${testContain}">
                                <form name="RemoveBookFromOrder" method="post"
                                      action="${pageContext.request.contextPath}/controller">
                                    <input type="hidden" name="command"
                                           value="remove_book_from_order">
                                    <input type="hidden" name="bookId" value="${book.id}">
                                    <button class="btn-sm" type="submit">
                                        <small>
                                            <fmt:message key="books.order.remove"/>
                                        </small>
                                    </button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form name="AddBookToOrder" method="post"
                                      action="${pageContext.request.contextPath}/controller">
                                    <input type="hidden" name="command" value="add_book_to_order">
                                    <input type="hidden" name="bookId" value="${book.id}">
                                    <input type="hidden" name="book" value="${book}">
                                    <div class="input-group-sm">
                                        <select class="form-select-sm" name="days">
                                            <option selected><small><fmt:message key="books.order.choose"/></small>
                                            </option>
                                            <option value="1"><fmt:message key="books.order.oneDay"/></option>
                                            <option value="7"><fmt:message key="books.order.oneWeek"/></option>
                                            <option value="30"><fmt:message key="books.order.oneMonth"/></option>
                                        </select>
                                        <button class="btn-sm" type="submit">
                                            <small>
                                                <fmt:message key="books.order.button"/>
                                            </small>
                                        </button>
                                    </div>
                                </form>
                            </c:otherwise>
                        </c:choose>
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
                       href="${pageContext.request.contextPath}/controller?command=empty_command&currentPage=${sessionScope.page.currentPage-1}">
                        <fmt:message key="main.prev"/></a>
                </li>
            </c:if>
            <c:forEach begin="1" end="${sessionScope.page.pageAmount}" varStatus="loop">
                <li class="page-item">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/controller?command=empty_command&currentPage=${loop.index}">
                            ${loop.index}</a>
                </li>
            </c:forEach>
            <c:if test="${sessionScope.page.currentPage ne sessionScope.page.pageAmount}">
                <li class="page-item">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/controller?command=empty_command&currentPage=${sessionScope.page.currentPage+1}">
                        <fmt:message key="main.next"/></a>
                </li>
            </c:if>
        </ul>
    </div>
    <%--TODO modal <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                                data-bs-target="#exampleModal"> --%>
    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel"><fmt:message key="books.order.caption"/></h5>
                    <fmt:message key="close" var="close"/>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="${close}"></button>
                </div>
                <div class="modal-body">

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
                    <button type="button" class="btn btn-primary">Сохранить изменения</button>
                </div>
            </div>
        </div>
    </div>
    ${requestScope.wrongDuration}
    <c:if test="${not empty requestScope.wrongDuration}">
        <div class="alert alert-danger d-flex align-items-center" role="alert">
            <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Danger:">
                <use xlink:href="#exclamation-triangle-fill"/>
            </svg>
            <div>
                    ${requestScope.wrongDuration}
            </div>
        </div>
    </c:if>

    <a href="${pageContext.request.contextPath}/controller?command=logout">Logout</a>
</div>
</body>
</html>
