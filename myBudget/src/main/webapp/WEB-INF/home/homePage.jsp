<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous"/>
	
	<spring:url value="/resources/homeStyle.css" var="css" />
	<link href="${css}" rel="stylesheet" />
	
	<title>Dashboard</title>
</head>
<body>
<%--     <h1>Hello <c:out value="${currentUser.firstName} ${currentUser.lastName}"></c:out></h1> --%>
    
    <main>
    	
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
    	
    	<div id="budget-tables-container">
    		<div id="free-to-spend">
    			<h5>Free to Spend</h5>
				<table class="table table-dark table-striped table-hover table-bordered">
	    			<tr>
	    				<th>Week</th>
	    				<th>Month</th>
	    			</tr>
	    			<tr>
	    				<td>${freeToSpendMonth/4}</td>
	    				<td>${freeToSpendMonth}</td>
	    			</tr>
	    		</table>
    		</div>
    		<div id="balance">
    			<h5>Balance</h5>
				<table class="table table-dark table-striped table-hover table-bordered">
	    			<tr>
	    				<th>Balance</th>
	    			</tr>
	    			<tr>
	    				<td>${userBalance}</td>
	    			</tr>
	    		</table>
    		</div>
    	</div>
		<div id="expenses-table">
	  		<h5>Expenses</h5>
	  		<div id="expense-table-container">
	  			<table class="table table-dark table-striped table-hover table-bordered">
		   			<tr>
		   				<th>Name</th>
		   				<th>Amount</th>
		   				<th>Due Date</th>
		   			</tr>
		   			<c:forEach items="${expenses}" var="exp">
			   			<tr>
			   				<td>${exp.title}</td>
			   				<td>${exp.amount}</td>
			   				<td>
				   				<c:forEach items="${exp.dueDates}" var="duedate">
				   					<span><a href="/duedate/${duedate.id}/${exp.id}">${duedate.date}</a></span>
				   				</c:forEach>
			   				</td>
			   			</tr>
		   			</c:forEach>
		   		</table>
	  		</div>
    	</div>
    	
    </main>
    
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-p34f1UUtsS3wqzfto5wAAmdvj+osOnFyQFpp4Ua3gs/ZVWx6oOypYoCJhGGScy+8" crossorigin="anonymous"></script>
</body>
</html>