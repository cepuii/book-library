<%@ include file="/jsp/frgments/pageSettings.jspf" %>
<%@ include file="/jsp/frgments/taglibs.jspf" %>

<!doctype html>
<html lang="en">
<head>
    <jsp:include page="/jsp/frgments/headTag.jspf"/>
</head>
<body>
<div class="container p-5 my-5 border">
    <h5>Welcome to the library</h5>
    <hr>
    <form name="LoginForm" method="post">
        <label>
            Login:
            <input type="text" name="email" value=""/>
        </label>
        <br/>
        <label>
            Password:
            <input type="password" name="password" value=""/>
        </label>
        <br/>


        ${errorLoginPassMessage}


        ${wrongAction}
        ${nullPage}
        <br/>
        <input type="submit"
               formaction="${pageContext.request.contextPath}/controller?command=login"
               value="Login"/>
        <input type="submit"
               formaction="${pageContext.request.contextPath}/controller?command=sign_up"
               value="Sign up"/>
        <%--            <input type="submit" value="Continue without authorisation" >--%>
    </form>
</div>
</body>
</html>
