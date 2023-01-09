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
<title><fmt:message key="main.title"/></title>
<jsp:include page="/jsp/frgments/headTag.jspf"/>
<body>

<div class="container">

    <jsp:include page="/jsp/frgments/bodyHeader.jsp"/>


    <div class="container">

        Books catalog
        <c:if test="${empty sessionScope.books}">
            <jsp:include page="/controller?command=empty_command&currentPage=1"/>
        </c:if>
        <table class="table table-dark table-striped">
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
                                    <input type="submit" value="Remove from order">
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form name="AddBookToOrder" method="post"
                                      action="${pageContext.request.contextPath}/controller">
                                    <input type="hidden" name="command" value="add_book_to_order">
                                    <input type="hidden" name="bookId" value="${book.id}">
                                    <input type="submit" value="Add to order">
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <ul class="pagination justify-content-center" style="margin:20px 0">
            <li class="page-item"><a class="page-link"
                                     href="controller?command=empty_command&currentPage=${requestScope.currentPage-1}"><fmt:message
                    key="main.prev"/></a></li>
            <li class="page-item"><a class="page-link"
                                     href="controller?command=empty_command&currentPage=${requestScope.currentPage+1}"><fmt:message
                    key="main.next"/></a></li>
        </ul>
    </div>


    <a href="
            ${pageContext.request.contextPath}/controller?command=logout">Logout</a>
</div>
</body>
</html>
