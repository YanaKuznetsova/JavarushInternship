<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Meals</title>
</head>

<body>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<section>
    <h3><spring:message code="${meal.isNew() ? 'meal.add' : 'meal.edit'}"/></h3>
    <hr>
    <form method="post" action='meals' name="frmAddMeal">
        <input type="hidden" name="id" value="${mealToEdit.id}">
        <spring:message code="meal.dateTime"/>: <input type="datetime-local" name="dateTime"
                                                       value="<c:out value="${mealToEdit.dateTime}"/>"/> <br/>
        <spring:message code="meal.description"/>: <input type="text" name="description"
                                                          value="<c:out value="${mealToEdit.description}"/>"/> <br/>
        <spring:message code="meal.calories"/>: <input type="number" name="calories"
                                                       value="<c:out value="${mealToEdit.calories}" />"/> <br/>

        <button type="submit"><spring:message code="common.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="common.cancel"/></button>
    </form>

</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>