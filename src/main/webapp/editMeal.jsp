<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.jsp">Home</a></h3>
<h2>Meals</h2>
<form method="POST" action='meals' name="frmAddMeal">
    ID: <input style="${mealToEdit.id > 0 ? 'display:show':'display:none'}" type="number"
               readonly="readonly" name="id" value="<c:out value="${mealToEdit.id}"/>"/> <br/>
    Date: <input type="datetime-local" name="dateTime" value="<c:out value="${mealToEdit.dateTime}"/>"/> <br/>
    Description: <input type="text" name="description" value="<c:out value="${mealToEdit.description}"/>"/> <br/>
    Calories: <input type="number" name="calories" value="<c:out value="${mealToEdit.calories}" />" /> <br />
    <input type="submit" value="Submit" />
</form>
</body>
</html>