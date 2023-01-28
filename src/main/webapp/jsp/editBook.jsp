<%@ include file="/jsp/fragments/pageSettings.jsp" %>
<%@ include file="/jsp/fragments/taglibs.jsp" %>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/fragments/headTag.jsp"/>
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

    <jsp:include page="/jsp/fragments/showResult.jsp"/>
    <div class="justify-content-center ">
        <form method="post">
            <input type="hidden" name="bookId" value="${requestScope.book==null ? 0 : requestScope.book.id}">
            <div class="mb-3">
                <label for="title" class="form-label"><fmt:message key="books.title"/>
                    <input type="text" class="form-control" id="title" name="title" placeholder="${placeholderTitle}"
                           title="<fmt:message key="books.required.title"/>" required
                           value="${requestScope.book.title}">
                </label>
            </div>
            <div class="mb-3">
                <label for="pt" class="form-label"><fmt:message key="books.publicationType"/>
                    <select class="form-select" name="publicationType" id="pt" required>
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
                           pattern="^\d{4}$"
                           value="${requestScope.book.datePublication}" required>
                </label>
            </div>
            <div class="mb-3">
                <label for="total" class="form-label">
                    <fmt:message key="books.total"/>
                    <input type="number" class="form-control" id="total" name="total" placeholder="${placeholderTotal}"
                           pattern="^\d{4}$"
                           value="${requestScope.book.total}" required>
                </label>
            </div>
            <div class="mb-3">
                <label for="fine" class="form-label">
                    <fmt:message key="books.fine"/>
                    <input type="number" class="form-control" id="fine" name="fine" placeholder="${placeholderFine}"
                           pattern="^\d{4}$"
                           value="${requestScope.book.fine}" required>
                </label>
            </div>
            <div class="mb-xxl-0">
                <div class="container">
                    <label class="form-label"><fmt:message key="books.authors"/>
                        <c:forEach var="author" items="${requestScope.book.authors}" varStatus="status">
                            <input type="hidden" name="authorId" value="${author.id}">
                            <input type="text" class="form-control" name="authorName"
                                   value="${author.name}" required>
                        </c:forEach>
                        <%--                        <c:choose>--%>
                        <%--                            <c:when test="${requestScope.book!=null}">--%>
                        <%--                                <div class="input-group mb-3">--%>
                        <%--                                    <input type="hidden" name="bookId" value="${requestScope.book.id}">--%>
                        <%--                                    <c:set var="inputName"><fmt:message key="books.edit.add.author"/></c:set>--%>
                        <%--                                    <input type="text" class="form-control" placeholder="${inputName}" name="newAuthor">--%>
                        <%--                                    <button class="btn btn-outline-secondary" type="submit" id="button-addon2"--%>
                        <%--                                            formaction="${pageContext.request.contextPath}/controller?command=add_author">--%>
                        <%--                                        <fmt:message key="add"/></button>--%>
                        <%--                                </div>--%>
                        <%--                            </c:when>--%>
                        <%--                            <c:otherwise>--%>
                        <%--                                <input type="hidden" name="authorId" value="0">--%>
                        <%--                                <input type="text" class="form-control" placeholder="${placeholderAuthors}"--%>
                        <%--                                       name="authorName">--%>
                        <%--                            </c:otherwise>--%>
                        <%--                        </c:choose>--%>
                        <div id="new-providers" class="form-label mb-2">
                            <div class="provider-append mb-2 ">
                                <c:if test="${empty requestScope.book.authors}">
                                    <div class="form-group mb-2">
                                        <input type='text' class='form-control' name='newAuthor' required>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        <button type="button" class="btn btn-primary" onclick="createNewElement()">
                            <fmt:message key="books.edit.add.author"/></button>
                    </label>
                </div>
            </div>
            <input type="submit" value="Save"
                   formaction="${pageContext.request.contextPath}/controller?command=save_book">
        </form>
    </div>
    <script type="text/JavaScript">
        function createNewElement() {
            // First create a DIV element.
            var txtNewInputBox = document.createElement('div');
            txtNewInputBox.className = "form-group mb-2";
            // Then add the content (a new input box) of the element.
            txtNewInputBox.innerHTML = "<input type='text' class='form-control' name='newAuthor' required>";

            // Finally put it where it is supposed to appear.
            document.getElementById("new-providers").appendChild(txtNewInputBox);
        }
    </script>

    <jsp:include page="/jsp/fragments/footer.jsp"/>
</div>

</body>
</html>
