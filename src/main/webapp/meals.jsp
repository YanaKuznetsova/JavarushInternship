<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setBundle basename="messages.app"/>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<style>
    .normal {
        color: green;
    }

    .excess {
        color: red;
    }
</style>

<head>
    <title>Meals</title>
</head>

<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

<table>
    <form method="POST" action="meals?action=filter" name="filterMealsByDate">
        <tr>
            <td>From date:</td>
            <td><input type="date" name="startDate" value="${param.startDate}"></td>
            <td>From time:</td>
            <td><input type="time" name="startTime" value="${param.startTime}"></td>
        </tr>
        <tr>
            <td>To date:</td>
            <td><input type="date" name="endDate" value="${param.endDate}"></td>
            <td>To time:</td>
            <td><input type="time" name="endTime" value="${param.endTime}"></td>
        </tr>
        <tr>
            <button type="submit">Filter</button>
        </tr>
    </form>
</table>

<table border="0" cellspacing="2" cellpadding="4">
    <tr style="font-weight: bold">
        <td>Id</td>
        <td>Date</td>
        <td>Description</td>
        <td>Calories</td>
    </tr>
    <c:forEach items="${mealsList}" var="meal">
        <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealTo"/>
        <tr class="${meal.exceed ? 'excess' : 'normal'}">
            <td>${meal.id}</td>
            <td>${fn:formatDateTime(meal.dateTime)}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<p><a href="meals?action=insert">Add Meal</a></p>

</body>
<jsp:include page="fragments/footer.jsp"/>
</html>