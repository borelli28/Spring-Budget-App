<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Login Page</title>
</head>
<body style="display: flex; justify-content: center; flex-wrap: wrap; text-align: center">
    <h1 style="width: 100%">Login</h1>
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
        <input type="submit" value="Login!"/>
    </form>
</body>
</html>