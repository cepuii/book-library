<%@ include file="/jsp/fragments/pageSettings.jspf" %>
<%@ include file="/jsp/fragments/taglibs.jspf" %>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/fragments/headTag.jspf"/>
    <title><fmt:message key="books.catalog.edit"/></title>
</head>
<body>
<c:set var="placeholderTitle" scope="request"><fmt:message key="books.edit.placeholder.title"/></c:set>
<c:set var="placeholderDate" scope="request"><fmt:message key="books.edit.placeholder.datePublication"/></c:set>
<c:set var="placeholderTotal" scope="request"><fmt:message key="books.edit.placeholder.total"/></c:set>
<c:set var="placeholderFine" scope="request"><fmt:message key="books.edit.placeholder.fine"/></c:set>
<c:set var="placeholderAuthors" scope="request"><fmt:message key="books.edit.placeholder.authors"/></c:set>
<div class="container">

    <jsp:include page="/jsp/fragments/bodyHeader.jsp"/>

    <div class="justify-content-center ">
        <form method="post">
            <input type="hidden" name="bookId" value="${requestScope.book==null ? 0 : requestScope.book.id}">
            <div class="mb-3">
                <label for="title" class="form-label"><fmt:message key="books.title"/>
                    <input type="text" class="form-control" id="title" name="title" placeholder="${placeholderTitle}"
                           value="${requestScope.book.title}">
                </label>
            </div>
            <div class="mb-3">
                <label for="pt" class="form-label"><fmt:message key="books.publicationType"/>
                    <select class="form-select" name="publicationType" id="pt">
                        <option value="BOOK" ${requestScope.book.publicationType eq '' ? 'selected':''}>Choose type of
                            publication
                        </option>
                        <option value="BOOK" ${requestScope.book.publicationType eq 'BOOK' ? 'selected':''}>Book
                        </option>
                        <option value="JOURNAL" ${requestScope.book.publicationType eq 'JOURNAL' ? 'selected':''}>
                            Journal
                        </option>
                        <option value="ARTICLE" ${requestScope.book.publicationType eq 'ARTICLE' ? 'selected':''}>
                            Article
                        </option>
                        <option value="NEWSPAPER" ${requestScope.book.publicationType eq 'NEWSPAPER' ? 'selected':''}>
                            Newspaper
                        </option>
                    </select>
                </label>
            </div>
            <div class="mb-3">
                <label for="date-publication" class="form-label">
                    <fmt:message key="books.datePublication"/>
                    <input type="number" class="form-control" id="date-publication" name="datePublication"
                           placeholder="${placeholderDate}"
                           value="${requestScope.book.datePublication}">
                </label>
            </div>
            <div class="mb-3">
                <label for="date-publication" class="form-label">
                    <fmt:message key="books.total"/>
                    <input type="number" class="form-control" id="total" name="total" placeholder="${placeholderTotal}"
                           value="${requestScope.book.total}">
                </label>
            </div>
            <div class="mb-3">
                <label for="fine" class="form-label">
                    <fmt:message key="books.fine"/>
                    <input type="number" class="form-control" id="fine" name="fine" placeholder="${placeholderFine}"
                           value="${requestScope.book.fine}">
                </label>
            </div>
            <div class="mb-xxl-0">
                <div class="container">
                    <label class="form-label"><fmt:message key="books.authors"/>
                        <%--                        <c:forTokens var="author" items="${book.authors}" delims="," varStatus="status">--%>
                        <%--                            <input type="text" class="form-control" id="authorSet" name="authorSet"--%>
                        <%--                                   value="${author.name}">--%>
                        <%--                        </c:forTokens>--%>

                        <%--                        <jsp:useBean id="author" class="ua.od.cepuii.library.entity.Author"/>--%>
                        <c:forEach var="author" items="${requestScope.book.authors}" varStatus="status">
                            <input type="hidden" name="authorId" value="${author.id}">
                            <input type="text" class="form-control" name="authorName"
                                   value="${author.name}">
                        </c:forEach>
                        <c:choose>
                            <c:when test="${requestScope.book!=null}">
                                <div class="input-group mb-3">
                                    <input type="hidden" name="bookId" value="${requestScope.book.id}">
                                    <c:set var="inputName"><fmt:message key="books.edit.add.author"/></c:set>
                                    <input type="text" class="form-control" placeholder="${inputName}" name="newAuthor">
                                    <button class="btn btn-outline-secondary" type="submit" id="button-addon2"
                                            formaction="${pageContext.request.contextPath}/controller?command=add_author">
                                        <fmt:message key="add"/></button>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <input type="hidden" name="authorId" value="0">
                                <input type="text" class="form-control" placeholder="${placeholderAuthors}"
                                       name="authorName">
                            </c:otherwise>
                        </c:choose>
                    </label>
                </div>
            </div>
            <input type="submit" value="Save"
                   formaction="${pageContext.request.contextPath}/controller?command=save_book">
        </form>
    </div>
<jsp:include page="/jsp/fragments/footer.jsp"/>
</div>

</body>
</html>
