<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous"/>

	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Description for Search Engines -->
	<meta name="Personal Finances Web App">
	<title>Edit Due Dates</title>
</head>
<body>

	<header>
		<h1>Due Dates</h1>
	</header>
	
	<main style="display: flex; justify-content: center">
		<table class="table-striped table-hover table-bordered">
   			<tr>
   				<th>Expense</th>
   				<th>Due Date</th>
   				<th>Actions</th>
   			</tr>
   			
   			<tr>
   				<td>${exp.title}</td>
   				<td>${duedate.date}</td>
   				<td style="display: flex; justify-content: space-between">
<%--    					<a href="/edit/duedate/${duedate.id}">Edit</a>
    				<p> | </p> --%>
					<form action="/delete/duedate/${duedate.id}" method="post">
						<input type="hidden" value="delete" name="_method" /> 
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						<button type="submit" class="btn btn-danger">Delete</button>
					</form>
   				</td>
   			</tr>
   		</table>
 
	</main>
	
</body>
</html>