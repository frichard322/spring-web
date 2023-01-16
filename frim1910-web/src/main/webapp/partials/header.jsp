<%@ page contentType="text/html;charset=UTF-8"%>
<%
    Object object = request.getSession(false).getAttribute("username");
    String username = null;
    if (object != null) {
        username = (String) object;
    }
%>
<header>
    <link href="../static/style.css" rel="stylesheet" type="text/css"/>
    <%if (username != null) {%>
        <%=username%>
        <form action="<%=request.getContextPath()%>/web/login" method="get">
            <input type="submit" value="Logout"/>
        </form>
    <%}%>
</header>
