<%@ include file="/jsp/frgments/taglibs.jspf" %>
<fmt:setBundle basename="text"/>
<header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
                <span class="navbar-brand">
                <img src="${pageContext.request.contextPath}/image/logo.png" alt="" width="90"
                     height="51" class="d-inline-block align-text-top">
                <fmt:message key="header.title"/>
            </span>

    <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
        <li><a href="${pageContext.request.contextPath}/index.jsp"
               class="nav-link px-2 link-secondary">Home</a></li>
        <c:if test="${sessionScope.userRole eq 'READER' || sessionScope.userRole eq 'LIBRARIAN'}">
            <li><a href="${pageContext.request.contextPath}/controller?command=show_orders"
                   class="nav-link px-2 link-dark"><fmt:message
                    key="orders.title"/> </a></li>
        </c:if>
        <c:if test="${sessionScope.userRole eq 'ADMIN'}">
            <li><a href="${pageContext.request.contextPath}/controller?command=show_users&modifaed=true"
                   class="nav-link px-2 link-dark"><fmt:message key="users.href"/> </a></li>
        </c:if>
    </ul>
    <div class="col-md-3 text-end">
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                ${sessionScope.user} hello!
            </c:when>
            <c:otherwise>
                <form>
                    <button type="submit" class="btn btn-outline-primary me-2"
                            formaction="${pageContext.request.contextPath}/jsp/login.jsp"><fmt:message
                            key="header.login"/>
                    </button>
                    <button type="submit" class="btn btn-primary"
                            formaction="${pageContext.request.contextPath}/jsp/signup.jsp"><fmt:message
                            key="header.signup"/>
                    </button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="col-mb-3 text-end">
        <form onchange="submit();">
            <label>
                <select name="language">
                    <option value="en" ${sessionScope.lang eq 'en' ? 'selected':''}><fmt:message
                            key="language.en"/></option>
                    <option value="uk" ${sessionScope.lang eq 'uk' ? 'selected':''}><fmt:message
                            key="language.ua"/></option>
                </select>
            </label>
        </form>
    </div>
    <hr/>
</header>