<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous"/>

	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Description for Search Engines -->
	<meta name="Personal Finances Web App">
	
	<spring:url value="/resources/incomeStyles/incomeStyle.css" var="css" />
	<link href="${css}" rel="stylesheet" />
	<title>Incomes</title>
</head>
<body>

   	<nav class="navbar navbar-dark bg-dark">
   		<div class="container-fluid">
    		<a id="home-tag" class="navbar-brand" href="/home">Dashboard</a>
    		<div id="manage-dropdown" class="dropdown">
			  <a class="btn btn-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-expanded="false">
			  	Manage
			  </a>
			
			  <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
			    <li><a class="dropdown-item" href="/cash-account-view">Cash Accounts</a></li>
			    <li><a class="dropdown-item" href="/expenses">Expenses</a></li>
			    <li><a class="dropdown-item" href="/income">Income</a></li>
			    <li><a class="dropdown-item" href="#">Investments</a></li>
			  </ul>
			</div>
    		<a id="account-tag" class="navbar-brand" href="/account">Account</a>
    		<form id="logoutForm" method="POST" action="/logout">
		        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		        <button type="submit" class="btn btn-danger">Logout</button>
		    </form>
	    </div>
   	</nav>

	<header>
		<h1>Income</h1>
	</header>
	
	<main style="display: flex; justify-content: center">
		<table class="table table-dark table-striped table-hover table-bordered">
   			<tr>
   				<th>Income Source</th>
   				<th>Total</th>
   				<th>Actions</th>
   			</tr>
			<c:if test="${incomes.isEmpty()}">
				<tr>
					<td>Income Source Name</td>
					<td>Null</td>
					<td></td>
				</tr>
			</c:if>
   			<c:forEach items="${incomes}" var="income">
	   			<tr>
	   				<td>${income.title}</td>
	   				<td>$${income.amount}</td>
	   				<td style="display: flex; justify-content: space-between">
	   					<a href="/edit/income/${income.id}">Edit</a>
	    				<p> | </p>
						<form action="/delete/income/${income.id}" method="post">
							<input type="hidden" value="delete" name="_method" /> 
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
							<button type="submit" class="btn btn-danger">Delete</button>
						</form>
	   				</td>
	   			</tr>
   			</c:forEach>
   		</table>
 
	</main>
	
	<div id="add-new-income">
		<h4>Add a New Income</h4>
		
		<p style="color: red"><form:errors path="income.*"/></p>
	    <c:forEach items="${errors}" var="error">
       		<p style="color: red"><c:out value="${error}"></c:out></p>
       	</c:forEach>
   
	    <form:form method="POST" action="/new/income" modelAttribute="income">
	    	<form:input path="user" type="hidden" value="${user.getId()}" />
	        <p>
	            <form:label path="title">Title:</form:label>
	            <form:input path="title"/>
	        </p>
	        <p>
	            <form:label path="amount">Amount: $</form:label>
	            <form:input type="number" step="0.01" path="amount"/>
	        </p>
	        <button type="submit" class="btn btn-primary">Create</button>
	    </form:form>
  	</div>
	
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-p34f1UUtsS3wqzfto5wAAmdvj+osOnFyQFpp4Ua3gs/ZVWx6oOypYoCJhGGScy+8" crossorigin="anonymous"></script>
</body>
</html> 