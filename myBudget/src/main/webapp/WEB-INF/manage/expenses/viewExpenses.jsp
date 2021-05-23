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
	
	<spring:url value="/resources/expStyles/expensesStyle.css" var="css" />
	<link href="${css}" rel="stylesheet" />
	<title>Expenses</title>
</head>
<body>

	<nav class="navbar navbar-dark bg-dark">
   		<div class="container-fluid">
    		<a class="navbar-brand" href="/home">Dashboard</a>
    		<div class="dropdown">
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
    		<a class="navbar-brand" href="/account">Account</a>
    		<form id="logoutForm" method="POST" action="/logout">
		        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		        <button type="submit" class="btn btn-danger">Logout</button>
		    </form>
	    </div>
   	</nav>

	<header>
		<h1>Expenses</h1>
	</header>
	
	<main style="display: flex; justify-content: center">
		<table class="table table-dark table-striped table-hover table-bordered">
   			<tr>
   				<th>Name</th>
   				<th>Amount Due</th>
   				<th>Actions</th>
   			</tr>
			<c:if test="${expenses.isEmpty()}">
				<tr>
					<td>Example</td>
					<td>$100.00</td>
					<td></td>
				</tr>
			</c:if>
   			<c:forEach items="${expenses}" var="exp">
	   			<tr>
	   				<td>${exp.title}</td>
	   				<td>$${exp.amount}</td>
	   				<td style="display: flex; justify-content: space-between">
	   					<a href="/edit/expenses/${exp.id}">Edit</a>
	    				<p id="separator">  |  </p>
						<form action="/delete/expenses/${exp.id}" method="post">
							<input type="hidden" value="delete" name="_method" /> 
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
							<button id="delete-btn" type="submit" class="btn btn-danger">Delete</button>
						</form>
	   				</td>
	   			</tr>
   			</c:forEach>
   		</table>
 
	</main>
	
	<div id="add-new-expense">
		<h4>Add a New Expense</h4>
		
		<p style="color: red"><form:errors path="expense.*"/></p>
	    <c:forEach items="${errors}" var="error">
       		<p style="color: red"><c:out value="${error}"></c:out></p>
       	</c:forEach>
   
	    <form:form method="POST" action="/new/expense" modelAttribute="expense">
	    	<form:input path="user" type="hidden" value="${user.getId()}" />
	        <p>
	            <form:label path="title">Title:</form:label>
	            <form:input path="title" required="true"/>
	        </p>
	        <p>
	            <form:label path="amount">Amount: $</form:label>
	            <form:input type="number" step="0.01" path="amount" required="true"/>
	        </p>
	        <button type="submit" class="btn btn-primary">Create</button>
	    </form:form>
  	</div>
	
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-p34f1UUtsS3wqzfto5wAAmdvj+osOnFyQFpp4Ua3gs/ZVWx6oOypYoCJhGGScy+8" crossorigin="anonymous"></script>
</body>
</html> 