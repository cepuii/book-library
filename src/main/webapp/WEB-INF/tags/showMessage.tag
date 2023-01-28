<%@ attribute name="error" %>

<%@ include file="/jsp/fragments/taglibs.jsp" %>

<c:if test="${not empty error}">
    <div class="alert alert-danger d-flex align-items-center small" role="alert">
        <div>
                ${error}
        </div>
    </div>
</c:if>

