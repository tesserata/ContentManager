<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<!--
Created by IntelliJ IDEA.
User: darya
Date: 27.10.13
Time: 18:54
-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>News Table</title></head>
<body>

<f:view>
    <%--
        <table border="2" width="2" cellspacing="2" cellpadding="2">
            <thead>
            <h3>News</h3>

            </thead>
            <tbody>
            <tr>
                <th>Content</th>
                <% for (News news : NewsDAO.INSTANCE_NEWS.getNews()) {%>
                <td><%=news.getContent()%></td>
                <%}%>
            </tr>


            <tr>
                <th>Date</th>
                <% for (News news : NewsDAO.INSTANCE_NEWS.getNews()) {%>
                <td><%=news.getDate()%></td>
                <%}%>
            </tr>

            <tr>
                <th>Source</th>
                <% for (News news : NewsDAO.INSTANCE_NEWS.getNews()) {%>
                <td><%=news.getSource()%></td>
                <%}%>
            </tr>


            <tr>
                <th>Status</th>
                <% for (News news : NewsDAO.INSTANCE_NEWS.getNews()) {%>
                <td><%switch (news.getStatus()){
                    case(0):
                        System.out.println("New");
                    case(1):
                        System.out.println("Posted");
                    case(-1):
                        System.out.println("Ignored");
                }%>

                </td>
                <%}%>
            </tr>

            </tbody>

        </table>
    --%>
</f:view>

</body>
</html>