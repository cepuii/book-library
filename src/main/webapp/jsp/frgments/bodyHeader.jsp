<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
                <span class="navbar-brand">
                <img src="${pageContext.request.contextPath}/image/logo.png" alt="" width="90"
                     height="51" class="d-inline-block align-text-top">
                Library
            </span>

    <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
        <li><a href="${pageContext.request.contextPath}/index.jsp"
               class="nav-link px-2 link-secondary">Home</a></li>
        <li><a href="#" class="nav-link px-2 link-dark">Features</a></li>
        <li><a href="#" class="nav-link px-2 link-dark">FAQs</a></li>
        <li><a href="#" class="nav-link px-2 link-dark">About</a></li>
    </ul>
    <div class="col-md-3 text-end">
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                ${sessionScope.user} hello!
            </c:when>
            <c:otherwise>
                <form>
                    <button type="submit" class="btn btn-outline-primary me-2"
                            formaction="${pageContext.request.contextPath}/jsp/login.jsp">Login
                    </button>
                    <button type="submit" class="btn btn-primary" formaction="${pageContext.request.contextPath}/jsp/signup.jsp">
                        Sign-up
                    </button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</header>