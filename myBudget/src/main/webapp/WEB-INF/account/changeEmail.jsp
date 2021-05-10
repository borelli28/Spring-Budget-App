<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
	<title>Change Email</title>
</head>
<body>
    <h1>Change Email</h1>
    <form:form method="post" action="/account/chn-email" modelAttribute="user">
    	<input value="put" name="_method" type="hidden" />
        <p>
            <form:label path="email" for="email">Email</form:label>
            <form:input path="email" name="email"/>
        </p>
        <button type="submit" class="btn btn-primary">Submit Change</button>
    </form:form>
</body>
</html>