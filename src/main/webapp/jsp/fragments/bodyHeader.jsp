<%@ include file="/jsp/fragments/pageSettings.jsp" %>
<%@ include file="/jsp/fragments/taglibs.jsp" %>

<hr/>
<header class="d-flex flex-wrap align-items-center justify-content-between  mt-1 border-bottom">
                <span class="navbar-brand align-items-center">
                <fmt:message key="header.title"/>
            </span>
    <ul class="nav col-12 col-md-auto mb-2 ms-3  mb-md-0">
        <li><a href="${pageContext.request.contextPath}/index.jsp"
               class="nav-link px-2 link-secondary">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/controller?command=show_books"
               class="nav-link px-2 link-secondary">Books catalog</a></li>
        <c:if test="${sessionScope.userRole eq 'READER' || sessionScope.userRole eq 'LIBRARIAN'}">
            <li><a href="${pageContext.request.contextPath}/controller?command=show_orders"
                   class="nav-link px-2 link-dark"><fmt:message
                    key="orders.title"/> </a></li>
        </c:if>
        <c:if test="${sessionScope.userRole eq 'ADMIN'}">
            <li><a href="${pageContext.request.contextPath}/controller?command=show_users&modified=true"
                   class="nav-link px-2 link-dark"><fmt:message key="users.href"/> </a></li>
        </c:if>
        <c:if test="${not empty sessionScope.userId}">
            <li><a href="${pageContext.request.contextPath}/controller?command=show_profile"
                   class="nav-link px-2 link-dark"><fmt:message key="profile"/> </a></li>
        </c:if>

    </ul>
    <c:choose>
        <c:when test="${not empty sessionScope.userId}">
            <div class="row justify-content-end">
                <div class="col px-2">
                        ${sessionScope.userEmail} hello!
                </div>
                <div class="col-2 px-2">
                    <form method="post" action="${pageContext.request.contextPath}/controller">
                        <input type="hidden" name="command" value="logout">
                        <button type="submit" class="btn btn-outline-primary"><fmt:message
                                key="header.logout"/></button>
                    </form>
                </div>
            </div>
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
</header>
<hr/>
<br>
