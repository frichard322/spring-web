<%@ page import="edu.bbte.idde.frim1910.backend.model.CarAdvertisement" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="com.fasterxml.jackson.core.type.TypeReference" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>Index</title>
</head>
<body>
    <%@ include file="partials/header.jsp" %>
    <%
        Collection<CarAdvertisement> carAdvertisements = new ObjectMapper()
            .readValue(
                (String) request.getAttribute("carAdvertisements"),
                new TypeReference<LinkedList<CarAdvertisement>>(){}
            );
    %>
    <%for (CarAdvertisement carAdvertisement : carAdvertisements) {%>
        <table>
            <tr>
                <td>ID:</td>
                <td><%= carAdvertisement.getUuid()%></td>
            </tr>
            <tr>
                <td>Title:</td>
                <td><%= carAdvertisement.getTitle()%></td>
            </tr>
            <tr>
                <td>Description:</td>
                <td><%= carAdvertisement.getDescription()%></td>
            </tr>
            <tr>
                <td>CarID:</td>
                <td><%= carAdvertisement.getCarUuid()%></td>
            </tr>
            <tr>
                <td>Price:</td>
                <td><%= carAdvertisement.getPrice()%></td>
            </tr>
        </table>
        <br>
        <br>
    <%}%>
</body>
</html>
