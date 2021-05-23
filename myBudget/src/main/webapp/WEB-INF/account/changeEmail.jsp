<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous"/>

	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Description for Search Engines -->
	<meta name="Personal Finances Web App">
	
	<spring:url value="/resources/accountStyles/editAccountStyle.css" var="css" />
	<link href="${css}" rel="stylesheet" />
	
	<title>Change Email</title>
</head>
<body>
    <h1>Change Email</h1>
    <div id="form-errors" style="color: red">
    	<c:forEach items="${userEmailErrors}" var="error">
    		<p><c:out value="${error.getDefaultMessage()}"></c:out></p>
    	</c:forEach>
    </div>
    <form:form method="post" action="/account/chn-email" modelAttribute="user">
    	<input value="put" name="_method" type="hidden" />
        <p>
            <form:label path="email" for="email">Email</form:label>
            <form:input path="email" name="email"/>
        </p>
        <div id="buttons">
	        <a href="/account">Go Back</a>
	        <button type="submit" class="btn btn-primary">Submit Change</button>
        </div>
    </form:form>
</body>
</html>