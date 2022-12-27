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
<html>
<head>
    <title>Main</title>
    <!-- Latest compiled and minified CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Latest compiled JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<h3> Welcome to the library</h3>
<hr/>
<c:if test="${not empty sessionScope.user}">
    ${sessionScope.user} hello!
</c:if>
<hr/>

<div class="container">
    Books catalog
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
        <jsp:useBean id="book" class="ua.od.cepuii.library.entity.Book"/>
        <c:forEach var="book" items="${requestScope.books}">
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
                            <form name="RemoveBookFromOrder" method="post" action="controller">
                                <input type="hidden" name="command" value="removeBookFromOrder">
                                <input type="hidden" name="bookId" value="${book.id}">
                                <input type="submit" value="Remove from order">
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form name="AddBookToOrder" method="post" action="controller">
                                <input type="hidden" name="command" value="addBookToOrder">
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


<a href="controller?command=logout">Logout</a>
</body>
</html>
