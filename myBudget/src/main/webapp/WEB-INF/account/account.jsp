<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous"/>

	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Description for Search Engines -->
	<meta name="Personal Finances Web App">
	
	<spring:url value="/resources/accountStyles/accountStyle.css" var="css" />
	<link href="${css}" rel="stylesheet" />
	
	<title>Your Account</title>
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

	<main>
	
		<table class="table table-dark table-striped table-hover table-bordered">
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
   			<form action="/account/chn-name">
   				<button class="btn btn-primary">Change Name</button>
   			</form>
   			<form action="/account/chn-email">
   				<button class="btn btn-primary">Change Email</button>
   			</form>
   			<form action="/account/chn-password">
   				<button class="btn btn-primary">Change Password</button>
   			</form>
   		</div>
	
	</main>
	
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-p34f1UUtsS3wqzfto5wAAmdvj+osOnFyQFpp4Ua3gs/ZVWx6oOypYoCJhGGScy+8" crossorigin="anonymous"></script>
</body>
</html>