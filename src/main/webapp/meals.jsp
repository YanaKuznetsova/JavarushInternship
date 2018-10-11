<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table border="0" cellspacing="2" cellpadding="4">
    <tr style="font-weight: bold">
        <td>Date</td>
        <td>Description</td>
        <td>Calories</td>
    </tr>
    <c:forEach items="${mealsList}" var="meal">
        <tr style="${meal.exceed == true ? 'color:red':'color:green'}">
            <td>${resultDateFormat.format(localDateTimeFormat.parse(meal.dateTime))}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>



