<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setBundle basename="messages.app"/>

<html>
<head>
    <title>Meals</title>
</head>

<body>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

<form method="POST" action='${pageContext.request.contextPath}/meals' name="frmAddMeal">
    ID: <input style="${mealToEdit.id != null ? 'display:show':'display:none'}" type="number"
               readonly="readonly" name="id" value="<c:out value="${mealToEdit.id}"/>"/> <br/>
    Date: <input type="datetime-local" name="dateTime" value="<c:out value="${mealToEdit.dateTime}"/>"/> <br/>
    Description: <input type="text" name="description" value="<c:out value="${mealToEdit.description}"/>"/> <br/>
    Calories: <input type="number" name="calories" value="<c:out value="${mealToEdit.calories}" />"/> <br/>
    <input type="submit" value="Submit"/>
</form>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>