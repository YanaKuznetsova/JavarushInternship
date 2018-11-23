<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>

<style>
    .normal {
        color: green;
    }

    .excess {
        color: red;
    }
</style>

<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<section>
    <h3><spring:message code="meal.title"/></h3>

    <table>
        <form method="post" action="meals/filter">
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
                <button type="submit"><spring:message code="meal.filter"/></button>
            </tr>
        </form>
    </table>

    <table border="0" cellspacing="2" cellpadding="4">
        <tr style="font-weight: bold">
            <td>Id</td>
            <th><spring:message code="meal.dateTime"/></th>
            <th><spring:message code="meal.description"/></th>
            <th><spring:message code="meal.calories"/></th>
            <th></th>
        </tr>

        <c:forEach items="${mealsList}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealTo"/>

            <tr class="${meal.exceed ? 'excess' : 'normal'}">
                <td>${meal.id}</td>
                <td>${fn:formatDateTime(meal.dateTime)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals/edit/<c:out value="${meal.id}"/>"><spring:message code="common.update"/></a></td>
                <td><a href="meals/delete/<c:out value="${meal.id}"/>"><spring:message code="common.delete"/></a></td>
            </tr>
        </c:forEach>
    </table>

    <hr>
    <a href="meals/add"><spring:message code="meal.add"/></a>
    <hr>

</section>
</body>

<jsp:include page="fragments/footer.jsp"/>
</html>