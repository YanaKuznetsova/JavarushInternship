<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Meals</title>
</head>

<body>
<h3><a href="index.jsp">Home</a></h3>
<h2>Meals</h2>

<table border="0" cellspacing="2" cellpadding="4">
    <tr style="font-weight: bold">
        <td>Id</td>
        <td>Date</td>
        <td>Description</td>
        <td>Calories</td>
    </tr>
    <c:forEach items="${mealsList}" var="meal">
        <tr style="${meal.exceed == true ? 'color:red':'color:green'}">
            <td>${meal.id}</td>
            <td>${dateTimeFormatter.format(meal.dateTime)}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<p><a href="meals?action=insert">Add Meal</a></p>

</body>
</html>