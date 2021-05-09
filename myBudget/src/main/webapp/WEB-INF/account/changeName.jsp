<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
	<title>Change Name</title>
</head>
<body>
    <h1>Change Name</h1>
    <form:form method="post" action="/account/chn-name" modelAttribute="user">
    	<input value="put" name="_method" type="hidden" />
        <p>
            <form:label path="firstName" for="firstName">First Name</form:label>
            <form:input path="firstName" name="firstName"/>
        </p>
		<p>
            <form:label path="lastName" for="lastName">Last Name</form:label>
            <form:input path="lastName" name="lastName"/>
        </p>
        <button type="submit" class="btn btn-primary">Submit Change</button>
    </form:form>
</body>
</html>