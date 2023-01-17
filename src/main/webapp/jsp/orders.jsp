<%@ include file="/jsp/frgments/pageSettings.jspf" %>
<%@ include file="/jsp/frgments/taglibs.jspf" %>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/frgments/headTag.jspf"/>
    <title><fmt:message key="orders.title"/></title>
</head>
<body>

<div class="container">
    <jsp:include page="/jsp/frgments/bodyHeader.jsp"/>
    <div class="container">
        <table class="table table-dark table-striped">
            <caption><fmt:message key="orders.title"/></caption>
            <thead>
            <tr>
                <th><fmt:message key="loan.book"/></th>
                <th><fmt:message key="loan.status"/></th>
                <th><fmt:message key="loan.start"/></th>
                <th><fmt:message key="loan.end"/></th>
                <th><fmt:message key="loan.action"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="loan" items="${requestScope.loans}">
                <tr>
                    <td>${loan.bookInfo}</td>
                    <td>${loan.status}</td>
                    <td><fmt:formatDate type="date" dateStyle="long" value="${loan.startDate}"/></td>
                    <td><fmt:formatDate type="date" dateStyle="long" value="${loan.endDate}"/></td>
                    <td>
                        <c:if test="${sessionScope.userRole eq 'READER'}">
                            <c:choose>
                                <c:when test="${loan.status eq 'RAW'}">
                                    <form name="RemoveBookFromOrder" method="post"
                                          action="${pageContext.request.contextPath}/controller">
                                        <input type="hidden" name="command"
                                               value="remove_book_from_order">
                                        <input type="hidden" name="bookId" value="${loan.bookId}">
                                        <input type="hidden" name="loanId" value="${loan.id}">
                                        <button class="btn-sm" type="submit">
                                            <fmt:message key="books.order.remove"/>
                                        </button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form name="RemoveBookFromOrder" method="post"
                                          action="${pageContext.request.contextPath}/controller">
                                        <input type="hidden" name="command"
                                               value="remove_book_from_order">
                                        <input type="hidden" name="bookId" value="${loan.bookId}">
                                        <input type="hidden" name="loanId" value="${loan.id}">
                                        <button class="btn-sm" type="submit" disabled>
                                            <fmt:message key="books.order.ready"/>
                                        </button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                        <c:if test="${sessionScope.userRole eq 'LIBRARIAN'}">
                            <form name="SetStatus" method="post"
                                  action="${pageContext.request.contextPath}/controller">
                                <input type="hidden" name="command"
                                       value="set_order_status">
                                <input type="hidden" name="loanId" value="${loan.id}">
                                <select name="loanStatus" id="inputAddress">
                                    <option value="COMPLETE" selected><fmt:message key="complete"/></option>
                                </select>
                                <button class="btn-sm" type="submit">
                                    <fmt:message key="books.order.setStatus"/>
                                </button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

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
