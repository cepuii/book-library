<%@ include file="/jsp/fragments/taglibs.jspf" %>
<%--       show exception if wrong action on page--%>
<c:if test="${not empty param.wrongAction}">
    <div class="alert alert-danger d-flex align-items-center" role="alert">
        <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Danger:">
        </svg>
        <div>
                ${param.wrongAction}
        </div>
    </div>
</c:if>
<c:if test="${not empty param.success}">
    <div class="alert alert-success d-flex align-items-center" role="alertdialog">
        <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Danger:">
        </svg>
        <div>
                ${param.success}
        </div>
    </div>
</c:if>
