<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous"/>

	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Description for Search Engines -->
	<meta name="Personal Finances Web App">

	<title>Login</title>
</head>
<body style="display: flex; justify-content: center; flex-wrap: wrap; text-align: center; background-color: #f2f2f2">
    <h1 style="width: 100%;margin: 3% 0">Welcome, Please log in into your account</h1>
    <form method="POST" action="/login">
        <p>
            <label for="email">Email</label>
            <input id="email" name="username"/>
        </p>
        <p>
            <label for="password">Password</label>
            <input type="password" id="password" name="password"/>
        </p>
		<!-- CSRF token -->
        <!-- We only need to add this token in forms that don't use jstl tags(form:label, form:input, etc.)-->
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="submit" value="Login" class="btn btn-primary" style="background-color: #0568A6"/>
    </form>
    <a id="reg-link" href="/registration" style="width: 100%; margin-top: 3%;color: #0568A6">or Register Here</a>
</body>
</html>