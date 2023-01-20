<%@ include file="/jsp/fragments/pageSettings.jspf" %>
<%@ include file="/jsp/fragments/taglibs.jspf" %>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/fragments/headTag.jspf"/>
    <title><fmt:message key="orders.title"/></title>
</head>
<body>

<div class="container">
    <jsp:include page="/jsp/fragments/bodyHeader.jsp"/>
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
                                <input type="hidden" name="bookId" value="${loan.bookId}">
                                <select name="loanStatus">
                                    <c:choose>
                                        <c:when test="${loan.status eq 'RAW'}">
                                            <option value="COMPLETE" selected><fmt:message key="complete"/></option>
                                        </c:when>
                                        <c:when test="${loan.status eq 'COMPLETE'}">
                                            <option value="RETURNED" selected><fmt:message key="return"/></option>
                                        </c:when>
                                    </c:choose>
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

<jsp:include page="/jsp/fragments/showResult.jsp"/>

</body>
</html>
