<%@ include file="/jsp/fragments/pageSettings.jspf" %>
<%@ include file="/jsp/fragments/taglibs.jspf" %>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/fragments/headTag.jspf"/>
    <title><fmt:message key="books.edit.add.librarian"/></title>
</head>
<body>

<div class="container">

    <jsp:include page="/jsp/fragments/bodyHeader.jsp"/>

    <div class="container">
        <form class="row g-3" method="post" action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="save_user">
            <div class="col-md-6">
                <label for="inputEmail4" class="form-label"><fmt:message key="users.add.email"/> </label>
                <input type="email" class="form-control" id="inputEmail4" name="email"
                       value="${requestScope.newUser.email}">
            </div>
            <div class="col-md-6">
                <label for="inputPassword4" class="form-label"><fmt:message key="users.add.password"/> </label>
                <input type="password" class="form-control" id="inputPassword4" name="password"
                       placeholder="Enter password">
            </div>
            <div class="col-12">
                <label for="inputAddress" class="form-label"><fmt:message key="users.role"/></label>
                <select name="role" id="inputAddress">
                    <option value="LIBRARIAN" selected><fmt:message key="users.filter.role.librarian"/></option>
                </select>
            </div>
            <%--            <div class="col-12">--%>
            <%--                <label for="inputAddress2" class="form-label">Адрес 2</label>--%>
            <%--                <input type="text" class="form-control" id="inputAddress2" placeholder="Квартира">--%>
            <%--            </div>--%>
            <%--            <div class="col-md-6">--%>
            <%--                <label for="inputCity" class="form-label">Город</label>--%>
            <%--                <input type="text" class="form-control" id="inputCity" placeholder="Брянск">--%>
            <%--            </div>--%>
            <%--            <div class="col-md-4">--%>
            <%--                <label for="inputState" class="form-label">Область</label>--%>
            <%--                <select id="inputState" class="form-select">--%>
            <%--                    <option selected>Выберите...</option>--%>
            <%--                    <option>...</option>--%>
            <%--                </select>--%>
            <%--            </div>--%>
            <%--            <div class="col-md-2">--%>
            <%--                <label for="inputZip" class="form-label">Индекс</label>--%>
            <%--                <input type="text" class="form-control" id="inputZip">--%>
            <%--            </div>--%>
            <%--            <div class="col-12">--%>
            <%--                <div class="form-check">--%>
            <%--                    <input class="form-check-input" type="checkbox" id="gridCheck">--%>
            <%--                    <label class="form-check-label" for="gridCheck">--%>
            <%--                        Проверить меня--%>
            <%--                    </label>--%>
            <%--                </div>--%>
            <%--            </div>--%>
            <div class="col-12">
                <button type="submit" class="btn btn-primary"><fmt:message key="save"/></button>
            </div>
        </form>
    </div>
    <jsp:include page="/jsp/fragments/showResult.jsp"/>
    <jsp:include page="/jsp/fragments/footer.jsp"/>
</div>
</body>
</html>
