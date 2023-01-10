<%@ include file="/jsp/frgments/taglibs.jspf" %>

<jsp:useBean id="book" class="ua.od.cepuii.library.entity.Book"/>

<c:set var="testContain" scope="page">
    <ctg:isContain bookId="${book.id}"/>
</c:set>
<c:choose>
    <c:when test="${testContain}">
        <form name="RemoveBookFromOrder" method="post"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command"
                   value="remove_book_from_order">
            <input type="hidden" name="bookId" value="${book.id}">
            <button class="btn-sm" type="submit">
                <small>
                    <fmt:message key="books.order.inOrder"/>
                </small>
            </button>
        </form>
    </c:when>
    <c:otherwise>
        <form name="AddBookToOrder" method="post"
              action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="add_book_to_order">
            <input type="hidden" name="bookId" value="${book.id}">
            <input type="hidden" name="book" value="${book}">
            <div class="input-group-sm">
                <label>
                    <select class="form-select-sm" name="days">
                        <option selected><fmt:message key="books.order.choose"/></option>
                        <option value="1"><fmt:message key="books.order.oneDay"/></option>
                        <option value="7"><fmt:message key="books.order.oneWeek"/></option>
                        <option value="30"><fmt:message key="books.order.oneMonth"/></option>
                    </select>
                </label>
                <button class="btn-sm" type="submit">
                    <small>
                        <fmt:message key="books.order.button"/>
                    </small>
                </button>
            </div>
        </form>
    </c:otherwise>
</c:choose>
