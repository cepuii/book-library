<%@ include file="/jsp/fragments/pageSettings.jsp" %>
<%@ include file="/jsp/fragments/taglibs.jsp" %>

<ul class="pagination justify-content-center" style="margin:20px 0">

    <c:if test="${requestScope.page.currentPage ne 1}">
        <li class="page-item">
            <a class="page-link"
               href="${pageContext.request.contextPath}/controller?command=${actionCommand}&currentPage=${requestScope.page.currentPage-1}">
                <fmt:message key="main.prev"/></a>
        </li>
    </c:if>
    <c:forEach begin="${requestScope.page.currentPage}"
               end="${requestScope.page.pageAmount > 5 ? 5 : requestScope.page.pageAmount}"
               varStatus="loop">
        <li class="page-item">
            <a class="page-link"
               href="${pageContext.request.contextPath}/controller?command=${actionCommand}&currentPage=${loop.index}">
                    ${loop.index}</a>
        </li>
    </c:forEach>
    <c:if test="${requestScope.page.pageAmount ne 0 && requestScope.page.currentPage ne requestScope.page.pageAmount}">
        <li class="page-item">
            <a class="page-link"
               href="${pageContext.request.contextPath}/controller?command=${actionCommand}&currentPage=${requestScope.page.currentPage+1}">
                <fmt:message key="main.next"/></a>
        </li>
    </c:if>
</ul>
