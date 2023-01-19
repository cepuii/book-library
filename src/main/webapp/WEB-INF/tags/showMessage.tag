<%@ attribute name="error" %>

<%@ include file="/jsp/fragments/taglibs.jspf" %>

<c:if test="${not empty error}">
    <div class="alert alert-danger d-flex align-items-center small" role="alert">
        <div>
                ${error}
        </div>
    </div>
</c:if>

