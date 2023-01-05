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

    <%--    <jsp:include page="frgments/bodyHeader.jsp"/>--%>
    <header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
                <span class="navbar-brand">
                <img src="${pageContext.request.contextPath}/image/logo.png" alt="" width="90"
                     height="51" class="d-inline-block align-text-top">
                Library
            </span>

        <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
            <li><a href="${pageContext.request.contextPath}/index.jsp"
                   class="nav-link px-2 link-secondary">Home</a></li>
            <li><a href="#" class="nav-link px-2 link-dark">Features</a></li>
            <li><a href="#" class="nav-link px-2 link-dark">FAQs</a></li>
            <li><a href="#" class="nav-link px-2 link-dark">About</a></li>
        </ul>
        <div class="col-md-3 text-end">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    ${sessionScope.user} hello!
                </c:when>
                <c:otherwise>
                    <form>
                        <button type="submit" class="btn btn-outline-primary me-2"
                                formaction="${pageContext.request.contextPath}/jsp/login.jsp">Login
                        </button>
                        <button type="submit" class="btn btn-primary" formaction="${pageContext.request.contextPath}/jsp/signup.jsp">
                            Sign-up
                        </button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </header>

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
    </div>


    <a href="${pageContext.request.contextPath}/controller?command=logout">Logout</a>
</div>
</body>
</html>
