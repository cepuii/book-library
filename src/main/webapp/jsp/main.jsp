<%--
  Created by IntelliJ IDEA.
  User: cepuii
  Date: 12/8/2022
  Time: 9:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="customtag" %>

<html lang="en">

<jsp:include page="frgments/headTag.jspf"/>

<body>

<div class="container">

    <jsp:include page="frgments/bodyHeader.jsp"/>

    <div class="container">

        Books catalog
        <c:if test="${empty sessionScope.books}">
            <jsp:include page="/controller?command=empty_command"/>
        </c:if>
        <table class="table table-dark table-striped">
            <thead>
            <tr>
                <th>title</th>
                <th>publicationType</th>
                <th>datePublication</th>
                <th>authors</th>
                <th>Order</th>
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
                                    <input type="hidden" name="command" value="remove_book_from_order">
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
    </div>


    <a href="${pageContext.request.contextPath}/controller?command=logout">Logout</a>
</div>
</body>
</html>
