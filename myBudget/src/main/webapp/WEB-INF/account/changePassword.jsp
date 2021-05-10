<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
	<title>Change Password</title>
</head>
<body>
    <h1>Change Password</h1>
    <div id="form-errors" style="color: red">
    	<c:forEach items="${userPassErrors}" var="error">
    		<p><c:out value="${error.getDefaultMessage()}"></c:out></p>
    	</c:forEach>
    </div>
    <form:form method="post" action="/account/chn-password" modelAttribute="user">
    	<input value="put" name="_method" type="hidden" />
        <p>
            <form:label path="password" for="password">Password</form:label>
            <form:input path="password" name="password"/>
        </p>
        <a href="/account">Go Back</a>
        <button type="submit" class="btn btn-primary">Submit Change</button>
    </form:form>
</body>
</html>