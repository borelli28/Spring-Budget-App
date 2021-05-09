<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
	<title>Your Account</title>
</head>
<body>

	<header>
		<h1>Hello, <c:out value="${user.firstName}"></c:out></h1>
	</header>
	<main>
	
		<table style="border: 1px solid black">
   			<tr>
   				<th>First Name</th>
   				<th>Last Name</th>
   				<th>Email</th>
   				<th>Password</th>
   			</tr>
   			<tr>
   				<td>${user.firstName}</td>
   				<td>${user.lastName}</td>
   				<td>${user.email}</td>
   				<td>*********</td>
   			</tr>
   		</table>
   		
   		<div id="make-changes-button">
   			<form action="/account/chn-name" style="margin: 1% 0">
   				<button class="btn btn-primary">Change Name</button>
   			</form>
   			<form action="/account/chn-email" style="margin: 1% 0">
   				<button class="btn btn-primary">Change Email</button>
   			</form>
   			<form action="/account/chn-password" style="margin: 1% 0">
   				<button class="btn btn-primary">Change Password</button>
   			</form>
   		</div>
	
	</main>
	
</body>
</html>