<%@ page contentType="text/html;charset=UTF-8"%>
<%
    String usernameMessage = (String) request.getAttribute("usernameMessage");
    String passwordMessage = (String) request.getAttribute("passwordMessage");
    String loginMessage = (String) request.getAttribute("loginMessage");
%>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <%@ include file="partials/header.jsp" %>
    <div class="error-message">
        <%if (usernameMessage != null) {%>
            <%=usernameMessage%>
        <%}%>
        <%if (passwordMessage != null) {%>
            <%=passwordMessage%>
        <%}%>
        <%if (loginMessage != null) {%>
            <%=loginMessage%>
        <%}%>
    </div>
    <form action="<%=request.getContextPath()%>/web/login" method="post">
        <label for="username">Username:</label>
        <input type="text" name="username" id="username"/>
        <br>
        <label for="password">Password:</label>
        <input type="password" name="password" id="password"/>
        <br>
        <input type="submit" value="Login"/>
    </form>
</body>
</html>
