<%@ include file="/jsp/fragments/pageSettings.jsp" %>
<%@ include file="/jsp/fragments/taglibs.jsp" %>

<ul class="pagination justify-content-center" style="margin:20px 0">
    <%--    previos button--%>
    <c:choose>
        <c:when test="${requestScope.page.currentPage ne 1}">
            <li class="page-item">
                <a class="page-link"
                   href="${pageContext.request.contextPath}/controller?command=${actionCommand}&currentPage=${requestScope.page.currentPage-1}">
                    <fmt:message key="main.prev"/></a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="page-item disabled">
                <a class="page-link"
                   href="${pageContext.request.contextPath}/controller?command=${actionCommand}&currentPage=${requestScope.page.currentPage-1}">
                    <fmt:message key="main.prev"/></a>
            </li>
        </c:otherwise>
    </c:choose>
    <%--    figures buttons--%>
    <c:forEach
            begin="${requestScope.page.currentPage < 3 ? 1 : Math.min((requestScope.page.currentPage - 2),(requestScope.page.pageAmount - 4))}"
            end="${requestScope.page.pageAmount}"
            varStatus="loop">
        <c:if test="${loop.count < 6}">
            <c:choose>
                <c:when test="${requestScope.page.currentPage == loop.index}">
                    <li class="page-item active">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/controller?command=${actionCommand}&currentPage=${loop.index}">
                                ${loop.index}</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/controller?command=${actionCommand}&currentPage=${loop.index}">
                                ${loop.index}</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:if>
    </c:forEach>

    <%--    next buttons--%>
    <c:choose>
        <c:when test="${requestScope.page.pageAmount ne 0 && requestScope.page.currentPage ne requestScope.page.pageAmount}">
            <li class="page-item">
                <a class="page-link"
                   href="${pageContext.request.contextPath}/controller?command=${actionCommand}&currentPage=${requestScope.page.currentPage+1}">
                    <fmt:message key="main.next"/></a>
            </li>
        </c:when>
        <c:otherwise>
            <li class="page-item disabled">
                <a class="page-link"
                   href="${pageContext.request.contextPath}/controller?command=${actionCommand}&currentPage=${requestScope.page.currentPage+1}">
                    <fmt:message key="main.next"/></a>
            </li>
        </c:otherwise>
    </c:choose>
</ul>
