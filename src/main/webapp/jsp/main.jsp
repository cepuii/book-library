<%--
  Created by IntelliJ IDEA.
  User: cepuii
  Date: 12/8/2022
  Time: 9:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/jsp/fragments/pageSettings.jspf" %>
<%@ include file="/jsp/fragments/taglibs.jspf" %>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/fragments/headTag.jspf"/>
    <title><fmt:message key="main.title"/></title>
</head>
<body>

<div class="container">

    <jsp:include page="/jsp/fragments/bodyHeader.jsp"/>
    <c:set var="actionCommand" value="show_books"/>


    <div class="container">
        Books catalog
        <c:if test="${empty requestScope.data}">
            <jsp:include page="/controller?command=show_books&currentPage=1&modifaed=true"/>
        </c:if>

        <%--sorting and filtering--%>
        <%--            https://getbootstrap.su/docs/5.0/forms/layout/--%>
        <div class="row">
            <div class="col-8">
                <form action="${pageContext.request.contextPath}/controller">
                    <div class="row">
                        <input type="hidden" name="command" value="${actionCommand}">
                        <input type="hidden" name="modified" value="true">
                        <div class="col">
                            <label>
                                <fmt:message key="books.filter.title"/>
                                <input type="text" name="titleSearch" value="${sessionScope.filter.firstParam}">
                            </label>
                        </div>
                        <div class="col">
                            <label>
                                <fmt:message key="books.filter.author"/>
                                <input type="text" name="authorSearch" value="${sessionScope.filter.secondParam}">
                                <%--                        <button type="submit"><fmt:message key="books.sort.button"/></button>--%>
                            </label>
                        </div>
                        <div class="col">
                            <label>
                                <fmt:message key="books.sort.name"/>
                                <select name="orderBy">
                                    <option value="b_title" ${sessionScope.filter.orderBy eq 'b_title' ? 'selected':''}>
                                        <fmt:message
                                                key="books.sort.title"/></option>
                                    <option value="pt_name" ${sessionScope.filter.orderBy eq 'pt_name' ? 'selected':''}>
                                        <fmt:message
                                                key="books.sort.type"/></option>
                                    <option value="b_date" ${sessionScope.filter.orderBy eq 'b_date' ? 'selected':''}>
                                        <fmt:message
                                                key="books.sort.date"/></option>
                                    <option value="authors" ${sessionScope.filter.orderBy eq 'authors' ? 'selected':''}>
                                        <fmt:message
                                                key="books.sort.author"/></option>
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
                    <input type="hidden" name="command" value="${actionCommand}">
                    <input type="hidden" name="modified" value="true">
                    <input type="hidden" name="cleanFilter" value="true">
                    <button type="submit" class="btn btn-outline-primary"><fmt:message
                            key="books.filter.cansel"/></button>
                </form>
            </div>
            <c:if test="${sessionScope.userRole eq 'ADMIN'}">
                <div class="col">
                    <form action="${pageContext.request.contextPath}/controller">
                        <input type="hidden" name="command" value="edit_book">
                        <input type="hidden" name="bookId" value="0">
                        <button type="submit" class="btn btn-primary"><fmt:message key="books.edit.add.book"/></button>
                    </form>
                </div>
            </c:if>
        </div>
        <hr/>

        <%--        books catalog--%>
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

            <%--            start loop by books--%>
            <c:forEach var="user" items="${requestScope.books}">
                <c:set var="authorsString" scope="request">
                    <ctg:getAuthors authorSet="${user.authors}"/>
                </c:set>
                <c:set var="authorsStringId" scope="request">
                    <ctg:getAuthors authorSet="${user.authors}"/>
                </c:set>
                <tr>
                    <td>${user.title}</td>
                    <td>${user.publicationType.name}</td>
                    <td>${user.datePublication}</td>
                    <td>${authorsString}</td>
                    <td>
                        <c:choose>
                            <%--buttons for READER--%>
                            <c:when test="${sessionScope.userRole eq 'READER'}">
                                <c:set var="testContain" scope="page">
                                    <ctg:isContain bookId="${user.id}"/>
                                </c:set>
                                <c:choose>
                                    <c:when test="${testContain}">
                                        <form name="RemoveBookFromOrder" method="post"
                                              action="${pageContext.request.contextPath}/controller">
                                            <input type="hidden" name="command"
                                                   value="remove_book_from_order">
                                            <input type="hidden" name="bookId" value="${user.id}">
                                            <button class="btn-sm" type="submit">
                                                <small>
                                                    <fmt:message key="books.order.inOrder"/>
                                                </small>
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <form name="AddBookToOrder" method="post"
                                              action="${pageContext.request.contextPath}/controller">
                                            <input type="hidden" name="command" value="add_book_to_order">
                                            <input type="hidden" name="bookId" value="${user.id}">
                                            <input type="hidden" name="book" value="${user}">
                                            <div class="input-group-sm">
                                                <div class="row">
                                                    <div class="col">
                                                        <label>
                                                            <select class="form-select-sm" name="days">
                                                                <option selected><fmt:message
                                                                        key="books.order.choose"/></option>
                                                                <option value="1"><fmt:message
                                                                        key="books.order.oneDay"/></option>
                                                                <option value="7"><fmt:message
                                                                        key="books.order.oneWeek"/></option>
                                                                <option value="30"><fmt:message
                                                                        key="books.order.oneMonth"/></option>
                                                            </select>
                                                        </label>
                                                    </div>
                                                    <div class="col">
                                                        <button class="btn-sm" type="submit">
                                                            <small>
                                                                <fmt:message key="books.order.button"/>
                                                            </small>
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <%--buttons for ADMIN--%>
                            <c:when test="${sessionScope.userRole eq 'ADMIN'}">
                                <div class="row">
                                    <div class="col">
                                        <form name="RemoveBook" method="post"
                                              action="${pageContext.request.contextPath}/controller">
                                            <input type="hidden" name="command"
                                                   value="remove_book">
                                            <input type="hidden" name="bookId" value="${user.id}">
                                            <button class="btn btn-outline-primary" type="submit">
                                                <small>
                                                    <fmt:message key="books.catalog.remove"/>
                                                </small>
                                            </button>
                                        </form>
                                    </div>
                                    <div class="col">
                                        <form name="EditBook" method="get"
                                              action="${pageContext.request.contextPath}/controller">
                                            <input type="hidden" name="bookId" value="${user.id}">
                                            <input type="hidden" name="command" value="edit_book">
                                            <button type="submit" class="btn btn-primary">
                                                <fmt:message key="books.catalog.edit"/>
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </c:when>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <ul class="pagination justify-content-center" style="margin:20px 0">

            <c:if test="${sessionScope.page.currentPage ne 1}">
                <li class="page-item">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/controller?command=${actionCommand}&currentPage=${sessionScope.page.currentPage-1}">
                        <fmt:message key="main.prev"/></a>
                </li>
            </c:if>
            <c:forEach begin="${sessionScope.page.currentPage > 2 ? sessionScope.page.currentPge - 1 : 1}"
                       end="${sessionScope.page.pageAmount > 5 ? 5 : sessionScope.page.pageAmount}"
                       varStatus="loop">
                <li class="page-item">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/controller?command=${actionCommand}&currentPage=${loop.index}">
                            ${loop.index}</a>
                </li>
            </c:forEach>
            <c:if test="${sessionScope.page.currentPage ne sessionScope.page.pageAmount}">
                <li class="page-item">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/controller?command=${actionCommand}&currentPage=${sessionScope.page.currentPage+1}">
                        <fmt:message key="main.next"/></a>
                </li>
            </c:if>
        </ul>
    </div>
    <jsp:include page="/jsp/fragments/showResult.jsp"/>
</div>

</body>
</html>