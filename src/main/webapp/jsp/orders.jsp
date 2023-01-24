<%@ include file="/jsp/fragments/pageSettings.jspf" %>
<%@ include file="/jsp/fragments/taglibs.jspf" %>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/fragments/headTag.jspf"/>
    <title><fmt:message key="orders.title"/></title>
</head>

<div class="container">

    <jsp:include page="/jsp/fragments/bodyHeader.jsp"/>

    <div class="container">

        <jsp:include page="/jsp/fragments/showResult.jsp"/>

        <table class="table table-dark table-striped">
            <caption><fmt:message key="orders.title"/></caption>
            <thead>
            <tr>
                <th><fmt:message key="loan.book"/></th>
                <th><fmt:message key="loan.status"/></th>
                <th><fmt:message key="loan.start"/></th>
                <th><fmt:message key="loan.end"/></th>
                <th><fmt:message key="users.fine"/></th>
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
                    <td>${loan.fine}</td>
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
                                        <button class="btn btn-primary" type="submit">
                                            <fmt:message key="books.order.remove"/>
                                        </button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-primary" type="submit" disabled>
                                        <fmt:message key="books.order.ready"/>
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                        <c:if test="${sessionScope.userRole eq 'LIBRARIAN'}">
                            <form name="SetStatus" method="post"
                                  action="${pageContext.request.contextPath}/controller">
                                <input type="hidden" name="command"
                                       value="set_order_status">
                                <input type="hidden" name="loanId" value="${loan.id}">
                                <input type="hidden" name="userId" value="${loan.userId}">
                                <input type="hidden" name="bookId" value="${loan.bookId}">
                                <c:choose>
                                    <c:when test="${loan.status eq 'RAW'}">
                                        <input type="hidden" name="loanStatus" value="COMPLETE">
                                        <input type="text" value="<fmt:message key="complete"/>" readonly="readonly">
                                    </c:when>
                                    <c:when test="${loan.status eq 'COMPLETE'}">
                                        <input type="hidden" name="loanStatus" value="RETURNED">
                                        <input type="text" value="<fmt:message key="return"/>" readonly="readonly">
                                    </c:when>
                                    <c:when test="${loan.status eq 'OVERDUE'}">
                                        <input type="hidden" name="fine" value="${loan.fine}">
                                        <input type="hidden" name="subtractFine" value="true">
                                        <input type="hidden" name="loanStatus" value="RETURNED">
                                        <input type="text" name="status" value="<fmt:message
                                            key="subtract"/>" readonly="readonly">
                                    </c:when>
                                </c:choose>
                                </select>
                                <button class="btn btn-primary" confirm="Are your sure?" type="submit">
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

    <jsp:include page="/jsp/fragments/footer.jsp"/>
</div>
</body>
</html>
