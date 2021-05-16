<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous"/>
	<title>Dashboard</title>
</head>
<body>
    <h1>Hello <c:out value="${currentUser.firstName} ${currentUser.lastName}"></c:out></h1>
    
    <main>
    	
    	<nav class="navbar navbar-light bg-light" style="border: 2px solid black">
    		<div class="container-fluid">
	    		<a class="navbar-brand" href="/home">Dashboard</a>
	    		<div class="dropdown">
				  <a class="btn btn-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-expanded="false">
				  	Manage
				  </a>
				
				  <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
				    <li><a class="dropdown-item" href="/cash-account-view">Cash Accounts</a></li>
				    <li><a class="dropdown-item" href="/expenses">Expenses</a></li>
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
    	
    	<div id="budget-tables-container" style="display: flex; justify-content: space-around; text-align: center; margin: 2% 1%">
    		<div id="free-to-spend">
    			<h5>Free to Spend</h5>
				<table style="border: 1px solid black; width: 30%" class="table-striped table-hover table-bordered">
	    			<tr>
	    				<th>Week</th>
	    				<th>Month</th>
	    			</tr>
	    			<tr>
	    				<td>$250</td>
	    				<td>$1000</td>
	    			</tr>
	    		</table>
    		</div>
    		<div id="balance">
    			<h5>Balance</h5>
				<table style="border: 1px solid black; width: 30%" class="table-striped table-hover table-bordered">
	    			<tr>
	    				<th>Balance</th>
	    			</tr>
	    			<tr>
	    				<td>${userBalance}</td>
	    			</tr>
	    		</table>
    		</div>
    	</div>
		<div id="expenses-table" style="text-align: center; margin: 2% 0">
	  		<h5>Expenses</h5>
			<table style="width: 80%; border: 1px solid black; margin: 0 10%" class="table-striped table-hover table-bordered">
	   			<tr>
	   				<th>Name</th>
	   				<th>Amount</th>
	   				<th>Due Date</th>
	   			</tr>
	   			<tr>
	   				<td>Car Payment</td>
	   				<td>$200</td>
	   				<td>15 May 2021</td>
	   			</tr>
	   		</table>
    	</div>
    	
    </main>
    
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-p34f1UUtsS3wqzfto5wAAmdvj+osOnFyQFpp4Ua3gs/ZVWx6oOypYoCJhGGScy+8" crossorigin="anonymous"></script>
</body>
</html>