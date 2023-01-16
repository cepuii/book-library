<%@ page import="ua.od.cepuii.library.entity.Book" %>
<%@ include file="/jsp/frgments/pageSettings.jspf" %>
<%@ include file="/jsp/frgments/taglibs.jspf" %>

<!doctype html>
<html lang="${sessionScope.lang}">
<head>
    <jsp:include page="/jsp/frgments/headTag.jspf"/>
    <title><fmt:message key="books.catalog.edit"/></title>
</head>
<body>

<div class="container">

    <jsp:include page="/jsp/frgments/bodyHeader.jsp"/>

    <div class="justify-content-center ">
        <form method="post">
            <input type="hidden" name="id" value="${requestScope.book.id}">
            <div class="mb-3">
                <label for="title" class="form-label"><fmt:message key="books.title"/>
                    <input type="text" class="form-control" id="title" name="title"
                           value="${book.title}">
                </label>
            </div>
            <div class="mb-3">
                <label for="pt" class="form-label"><fmt:message key="books.publicationType"/>
                    <select class="form-select" name="publicationType" id="pt">
                        <option value="BOOK" ${book.publicationType eq 'BOOK' ? 'selected':''}>Book</option>
                        <option value="JOURNAL" ${book.publicationType eq 'JOURNAL' ? 'selected':''}>Journal
                        </option>
                        <option value="ARTICLE" ${book.publicationType eq 'ARTICLE' ? 'selected':''}>Article
                        </option>
                        <option value="NEWSPAPER" ${book.publicationType eq 'NEWSPAPER' ? 'selected':''}>
                            Newspaper
                        </option>
                    </select>
                </label>
            </div>
            <div class="mb-3">
                <label for="date-publication" class="form-label">
                    <fmt:message key="books.datePublication"/>
                    <input type="number" class="form-control" id="date-publication" name="datePublication"
                           value="${book.datePublication}">
                </label>
            </div>
            <div class="mb-3">
                <label for="date-publication" class="form-label">
                    <fmt:message key="books.total"/>
                    <input type="number" class="form-control" id="total" name="total"
                           value="${book.total}">
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
                        <c:forEach var="author" items="${book.authors}" varStatus="status">
                            <input type="hidden" name="authorId" value="${author.id}">
                            <input type="text" class="form-control" name="authorName"
                                   value="${author.name}">
                        </c:forEach>
                        <div class="input-group mb-3">
                            <input type="hidden" name="bookId" value="${requestScope.book.id}">
                            <c:set var="inputName"><fmt:message key="books.edit.addAuthor"/></c:set>
                            <input type="text" class="form-control" placeholder="${inputName}" name="newAuthor">
                            <button class="btn btn-outline-secondary" type="submit" id="button-addon2"
                                    formaction="${pageContext.request.contextPath}/controller?command=add_author">
                                <fmt:message key="add"/></button>
                        </div>
                    </label>
                </div>
            </div>
            <input type="submit" value="Save"
                   formaction="${pageContext.request.contextPath}/controller?command=save_book">
        </form>
    </div>
</div>
</body>
</html>
