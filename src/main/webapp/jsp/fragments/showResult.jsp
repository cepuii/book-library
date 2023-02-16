<%@ include file="/jsp/fragments/pageSettings.jsp" %>
<%@ include file="/jsp/fragments/taglibs.jsp" %>
<%--       show exception if wrong action on page--%>
<div class="container">
    <c:if test="${not empty requestScope.reports.wrongAction}">
        <div class="alert alert-danger d-flex align-items-center" role="alert">
            <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Danger:">
            </svg>
            <div>
                <fmt:message key="${requestScope.reports.wrongAction}"/>
                <button type="button" class="btn-close end-100" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
    </c:if>
    <c:if test="${not empty requestScope.reports.success}">
        <div class="alert alert-success d-flex align-items-center" role="alertdialog">
            <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Danger:">
            </svg>
            <div>
                <fmt:message key="${requestScope.reports.success}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
    </c:if>
</div>
